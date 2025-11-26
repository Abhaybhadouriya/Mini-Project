package com.abhay.mapper;

import com.abhay.dto.CustomerRequest;
import com.abhay.dto.CustomerResponse;
import com.abhay.dto.FeesPayment;
import com.abhay.entity.Bill;
import com.abhay.entity.Customer;
import com.abhay.entity.StudentPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void testToCustomer_AllFieldsPresent() {
        CustomerRequest request = new CustomerRequest(
                9.5f, "IT", "test@test.com", "Alice", "Smith", 2025,
                "password123", "/path/to/photo.jpg", 50, "R100", "Cyber", 3.8f
        );

        Customer customer = customerMapper.toCustomer(request);

        // KILLS NULL_RETURNS MUTANT for toCustomer
        assertNotNull(customer);
        assertEquals("Alice", customer.getFirstName());
        assertEquals("test@test.com", customer.getEmail());
        assertEquals(9.5f, customer.getCgpa());
        assertEquals(2025, customer.getGraduationYear());
        assertEquals("R100", customer.getRollNumber());
        assertEquals(50, customer.getPlacementId());
    }

    @Test
    void testToCustomer_OptionalFieldsMissing() {
        CustomerRequest request = new CustomerRequest(
                null, null, "test@test.com", "Bob", null, null,
                "password123", null, null, "R101", null, null
        );

        Customer customer = customerMapper.toCustomer(request);

        // KILLS NULL_RETURNS MUTANT for toCustomer
        assertNotNull(customer);
        assertEquals(0.0f, customer.getCgpa());
        assertEquals(1900, customer.getGraduationYear());
        assertEquals(0.0f, customer.getTotalCredits());
        assertEquals(0, customer.getPlacementId());
        assertNull(customer.getLastName());
    }

    @Test
    void testToCustomerResponse_ShouldMapCustomer() {
        Customer customer = Customer.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@test.com")
                .build();

        CustomerResponse response = customerMapper.toCustomerResponse(customer);

        // KILLS NO_COVERAGE/NULL_RETURNS MUTANT for toCustomerResponse
        assertNotNull(response);
        assertEquals("Alice", response.firstName());
        assertEquals("Smith", response.lastName());
        assertEquals("alice@test.com", response.email());
    }

    @Test
    void testToEntity_FeesPaymentToStudentPayment() {
        FeesPayment feesPayment = new FeesPayment(
                10L, 1L, new BigDecimal("100.00"), "Semester Payment"
        );
        Customer mockStudent = Customer.builder().id(1L).build();
        Bill mockBill = Bill.builder().id(10L).build();

        StudentPayment payment = customerMapper.toEntity(feesPayment, mockStudent, mockBill);

        // KILLS NULL_RETURNS MUTANT for toEntity
        assertNotNull(payment);
        assertEquals(new BigDecimal("100.00"), payment.getAmount());
        assertEquals("Semester Payment", payment.getDescription());
        assertEquals(mockStudent, payment.getStudent());
        assertEquals(mockBill, payment.getBill());
        assertEquals(LocalDate.now(), payment.getPaymentDate());
    }
}