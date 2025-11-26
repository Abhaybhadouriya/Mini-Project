package com.abhay.service;

import com.abhay.dto.*;
import com.abhay.entity.Bill;
import com.abhay.entity.Customer;
import com.abhay.entity.StudentPayment;
import com.abhay.exception.CustomerNotFoundException;
import com.abhay.helper.EncryptionService;
import com.abhay.helper.JWTHelper;
import com.abhay.mapper.CustomerMapper;
import com.abhay.repo.BillRepo;
import com.abhay.repo.CustomerRepo;
import com.abhay.repo.StudentPaymentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private BillRepo billRepo;

    @Mock
    private StudentPaymentRepo studentPaymentRepo;

    @Mock
    private JWTHelper jwtHelper;

    @InjectMocks
    private CustomerService customerService;

    private Customer mockCustomerRaw;
    private CustomerRequest mockCustomerRequest;
    private Bill mockBill;

    @BeforeEach
    void setUp() {
        // Customer as returned by the Mapper (Raw Password)
        mockCustomerRaw = Customer.builder()
                .id(1L)
                .email("test@example.com")
                .password("rawPassword123") // Raw password initially
                .rollNumber("R101")
                .build();

        // Use POJO Constructor for Request
        mockCustomerRequest = new CustomerRequest(
                9.0f, "IT", "test@example.com", "Test", "User", 2025,
                "rawPassword123", null, 0, "R101", "Cyber", 4.0f
        );

        mockBill = Bill.builder()
                .id(10L)
                .amount(new BigDecimal("1000.00"))
                .build();
    }

    // --- Customer Registration Tests ---


    @Test
    void createCustomer_Success() {
        // Arrange
        String rawPassword = "rawPassword123";
        String encodedPassword = "hashedPasswordXYZ";

        // Make sure mapper returns a customer with RAW password
        Customer mappedCustomer = Customer.builder()
                .id(1L)
                .email("test@example.com")
                .password(rawPassword)
                .rollNumber("R101")
                .build();

        when(customerMapper.toCustomer(mockCustomerRequest)).thenReturn(mappedCustomer);

        // Encode will convert raw -> encoded
        when(encryptionService.encode(rawPassword)).thenReturn(encodedPassword);

        // We don't care what repo returns, just that it gets called with correct object
        when(customerRepo.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEntity<Object> response = customerService.createCustomer(mockCustomerRequest);

        // Assert status + body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("success", body.get("status"));
        assertEquals("test@example.com", body.get("customerId"));

        // Capture saved customer
        ArgumentCaptor<Customer> customerCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepo).save(customerCaptor.capture());
        Customer savedCustomer = customerCaptor.getValue();

        // 1) encode must be called with RAW password
        verify(encryptionService).encode(rawPassword);

        // 2) SAVED password must be the ENCODED one, not the raw one
        assertEquals(encodedPassword, savedCustomer.getPassword(), "Saved password must be encoded");
        assertNotEquals(rawPassword, savedCustomer.getPassword(), "Raw password must NOT be stored");
    }


    @Test
    void createCustomer_DataIntegrityViolation() {
        when(encryptionService.encode(anyString())).thenReturn("hashed");
        when(customerMapper.toCustomer(any())).thenReturn(mockCustomerRaw);
        when(customerRepo.save(any(Customer.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate"));

        ResponseEntity<Object> response = customerService.createCustomer(mockCustomerRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("error", body.get("status"));
        assertTrue(body.get("message").toString().contains("already exists"));
    }

    @Test
    void createCustomer_IllegalArgumentException() {
        when(customerMapper.toCustomer(any()))
                .thenThrow(new IllegalArgumentException("Bad input"));

        ResponseEntity<Object> response = customerService.createCustomer(mockCustomerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(customerRepo, never()).save(any());
    }

    @Test
    void createCustomer_GenericException() {
        when(customerMapper.toCustomer(any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<Object> response = customerService.createCustomer(mockCustomerRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // --- Retrieval Tests ---

    @Test
    void getCustomer_Success() {
        when(customerRepo.findByEmail("test@example.com")).thenReturn(Optional.of(mockCustomerRaw));

        Customer result = customerService.getCustomer("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getCustomer_NotFound() {
        when(customerRepo.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () ->
                customerService.getCustomer("unknown@example.com")
        );

        // Mutation Killer: Check exception message format
        String expectedMessage = format("Cannot update Customer:: No customer found with the provided ID:: %s", "unknown@example.com");
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void retrieveCustomer_Success() {
        when(customerRepo.findByEmail("test@example.com")).thenReturn(Optional.of(mockCustomerRaw));
        CustomerResponse mockResponse = new CustomerResponse("Test User", "test@example.com", "R101");
        when(customerMapper.toCustomerResponse(mockCustomerRaw)).thenReturn(mockResponse);

        CustomerResponse result = customerService.retrieveCustomer("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com","test@example.com");
    }

    // --- Response Helper Test ---

    @Test
    void generateResponse_WithToken() {
        ResponseEntity<Map<String, Object>> response = customerService.generateResponse(
                HttpStatus.OK, "Success", "sample_token"
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("sample_token", response.getBody().get("token"));
    }

    @Test
    void generateResponse_WithoutToken() {
        ResponseEntity<Map<String, Object>> response = customerService.generateResponse(
                HttpStatus.UNAUTHORIZED, "Failed", null
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().containsKey("token"));
    }

    // --- Login Tests ---

    @Test
    void login_Success() {
        // Arrange
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        // Customer in DB has HASHED password
        Customer dbCustomer = Customer.builder().email("test@example.com").password("hashedDBPass").build();

        when(customerRepo.findByEmail("test@example.com")).thenReturn(Optional.of(dbCustomer));
        when(encryptionService.validates("password123", "hashedDBPass")).thenReturn(true);
        when(jwtHelper.generateToken(dbCustomer)).thenReturn("jwt_token");

        // Act
        ResponseEntity<Map<String, Object>> response = customerService.login(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt_token", response.getBody().get("token"));

        // Mutation Killer: Ensure validation was called with correct args
        verify(encryptionService).validates("password123", "hashedDBPass");
    }

    @Test
    void login_InvalidPassword() {
        LoginRequest request = new LoginRequest("test@example.com", "wrong");
        Customer dbCustomer = Customer.builder().email("test@example.com").password("hashedDBPass").build();

        when(customerRepo.findByEmail("test@example.com")).thenReturn(Optional.of(dbCustomer));
        when(encryptionService.validates("wrong", "hashedDBPass")).thenReturn(false);

        ResponseEntity<Map<String, Object>> response = customerService.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid email or password", response.getBody().get("message"));
    }

    @Test
    void login_UserNotFound() {
        // Specifically tests the path where user is missing, ensuring it doesn't just crash but handles the exception via the global catch
        LoginRequest request = new LoginRequest("ghost@example.com", "any");
        when(customerRepo.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> response = customerService.login(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().get("message").toString().contains("An error occurred during login"));
    }

    @Test
    void login_GenericException() {
        LoginRequest request = new LoginRequest("test@example.com", "pass");
        when(customerRepo.findByEmail(anyString())).thenThrow(new RuntimeException("DB Error"));

        ResponseEntity<Map<String, Object>> response = customerService.login(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // --- Bill Tests ---

    @Test
    void getUnpaidBills_MappingTest() {
        long studentId = 1L;
        Object[] rawRow = new Object[]{
                studentId, "John", "Doe", 10L, "Tuition",
                new BigDecimal("5000.00"), Date.valueOf(LocalDate.of(2025, 1, 15)),
                new BigDecimal("2000.00"), new BigDecimal("3000.00")
        };

        when(customerRepo.findUnpaidBillsByStudentId(studentId))
                .thenReturn(Collections.singletonList(rawRow));

        List<UnpaidBills> result = customerService.GetunpaidBills(studentId);

        assertFalse(result.isEmpty());
        UnpaidBills bill = result.get(0);

        assertEquals(studentId, bill.getStudentId());
        assertEquals(new BigDecimal("3000.00"), bill.getRemainingBalance());
    }

    @Test
    void getPaidBills_MappingTest() {
        long studentId = 1L;
        Object[] rawRow = new Object[]{
                studentId, new BigDecimal("1000.00"), Date.valueOf(LocalDate.of(2024, 1, 1)),
                100L, new BigDecimal("4000.00"), "Tuition"
        };
        when(customerRepo.findpaidBillsByStudentId(studentId)).thenReturn(Collections.singletonList(rawRow));

        List<PaidBills> result = customerService.GetPaidBills(studentId);

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getBillId());
    }

    @Test
    void getBillsWithPayments_MixedScenarios() {
        long studentId = 1L;

        // Bill 1: Has 2 Payments
        Object[] rawRow1 = new Object[]{
                1L, "Tuition", new BigDecimal("5000.00"), "01/01/24", "15/01/24",
                new BigDecimal("3000.00"), new BigDecimal("2000.00"),
                101L, new BigDecimal("1000.00"), "05/01/24", "Partial 1"
        };
        Object[] rawRow2 = new Object[]{
                1L, "Tuition", new BigDecimal("5000.00"), "01/01/24", "15/01/24",
                new BigDecimal("3000.00"), new BigDecimal("2000.00"),
                102L, new BigDecimal("2000.00"), "10/01/24", "Partial 2"
        };

        // Bill 2: NO PAYMENTS
        Object[] rawRow3 = new Object[]{
                2L, "Library", new BigDecimal("500.00"), "01/02/24", "28/02/24",
                new BigDecimal("0.00"), new BigDecimal("500.00"),
                null, null, null, null
        };

        when(customerRepo.findBillsWithPaymentsByStudentId(studentId))
                .thenReturn(List.of(rawRow1, rawRow2, rawRow3));

        // Act
        List<BillWithPayments> result = customerService.getBillsWithPayments(studentId);

        // --- Top-level checks ---
        assertEquals(2, result.size(), "Should consolidate 3 rows into 2 distinct bills");

        // --- Bill 1 checks ---
        BillWithPayments bill1 = result.stream()
                .filter(b -> b.getBillId() == 1L)
                .findFirst()
                .orElseThrow();

        assertEquals(1L, bill1.getBillId());
        assertEquals("Tuition", bill1.getDescription());
        assertEquals(new BigDecimal("5000.00"), bill1.getAmount());
        assertEquals("01/01/24", bill1.getBillDate());
        assertEquals("15/01/24", bill1.getDeadline());

        // These two lines kill mutants 259 and 260 (remaining / paid)
        assertEquals(new BigDecimal("3000.00"), bill1.getRemaining(), "Remaining should match DB value");
        assertEquals(new BigDecimal("2000.00"), bill1.getPaid(), "Paid should match DB value");

        // Parts list must have 2 payments
        assertNotNull(bill1.getParts(), "Parts list must not be null");
        assertEquals(2, bill1.getParts().size(), "Bill 1 should have 2 payment parts");

        BillWithPayments.PaymentDetail p1 = bill1.getParts().get(0);
        BillWithPayments.PaymentDetail p2 = bill1.getParts().get(1);

        // Kill 266–269
        assertEquals(101L, p1.getPaymentId());
        assertEquals(new BigDecimal("1000.00"), p1.getAmount());
        assertEquals("05/01/24", p1.getPaymentDate());
        assertEquals("Partial 1", p1.getDescription());

        assertEquals(102L, p2.getPaymentId());
        assertEquals(new BigDecimal("2000.00"), p2.getAmount());
        assertEquals("10/01/24", p2.getPaymentDate());
        assertEquals("Partial 2", p2.getDescription());

        // --- Bill 2 checks (no payments) ---
        BillWithPayments bill2 = result.stream()
                .filter(b -> b.getBillId() == 2L)
                .findFirst()
                .orElseThrow();

        assertEquals(2L, bill2.getBillId());
        assertEquals("Library", bill2.getDescription());
        assertEquals(new BigDecimal("500.00"), bill2.getAmount());
        assertEquals("01/02/24", bill2.getBillDate());
        assertEquals("28/02/24", bill2.getDeadline());
        assertEquals(new BigDecimal("0.00"), bill2.getRemaining());
        assertEquals(new BigDecimal("500.00"), bill2.getPaid());

        // For bill without payments, parts list should be empty (or null treated as empty)
        assertTrue(bill2.getParts() == null || bill2.getParts().isEmpty(),
                "Bill 2 should have 0 payment parts");
    }


    // --- Fee Payment Tests ---

    @Test
    void payFees_Success() {
        FeesPayment paymentDto = new FeesPayment(10L, 1L, new BigDecimal("100"), "Desc");
        StudentPayment expectedPaymentEntity = new StudentPayment(); // Mock entity

        when(customerRepo.findById(1L)).thenReturn(Optional.of(mockCustomerRaw));
        when(billRepo.findById(10L)).thenReturn(Optional.of(mockBill));
        when(customerMapper.toEntity(eq(paymentDto), eq(mockCustomerRaw), eq(mockBill))).thenReturn(expectedPaymentEntity);

        ResponseEntity<Object> response = customerService.payFees(paymentDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Mutation Killer: Verify save is called with the EXACT object returned by mapper
        verify(studentPaymentRepo).save(expectedPaymentEntity);
    }

    @Test
    void payFees_Conflict() {
        FeesPayment paymentDto = new FeesPayment(10L, 1L, new BigDecimal("100"), "Desc");

        when(customerRepo.findById(1L)).thenReturn(Optional.of(mockCustomerRaw));
        when(billRepo.findById(10L)).thenReturn(Optional.of(mockBill));
        when(customerMapper.toEntity(any(), any(), any())).thenReturn(new StudentPayment());
        doThrow(new DataIntegrityViolationException("Exists")).when(studentPaymentRepo).save(any());

        ResponseEntity<Object> response = customerService.payFees(paymentDto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void payFees_BadRequest() {
        FeesPayment paymentDto = new FeesPayment(10L, 1L, new BigDecimal("100"), "Desc");

        when(customerRepo.findById(1L)).thenReturn(Optional.of(mockCustomerRaw));
        when(billRepo.findById(10L)).thenReturn(Optional.of(mockBill));
        when(customerMapper.toEntity(any(), any(), any())).thenThrow(new IllegalArgumentException("Bad arg"));

        ResponseEntity<Object> response = customerService.payFees(paymentDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void payFees_GenericException() {
        FeesPayment paymentDto = new FeesPayment(10L, 1L, new BigDecimal("100"), "Desc");
        when(customerRepo.findById(1L)).thenThrow(new RuntimeException("DB Down"));

        ResponseEntity<Object> response = customerService.payFees(paymentDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void payFees_CustomerNotFound() {
        FeesPayment paymentDto = new FeesPayment(10L, 99L, new BigDecimal("100"), "Desc");
        when(customerRepo.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = customerService.payFees(paymentDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("An unexpected error occurred"));
    }

    @Test
    void payFees_BillNotFound() {
        FeesPayment paymentDto = new FeesPayment(99L, 1L, new BigDecimal("100"), "Desc");
        when(customerRepo.findById(1L)).thenReturn(Optional.of(mockCustomerRaw));
        when(billRepo.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = customerService.payFees(paymentDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}