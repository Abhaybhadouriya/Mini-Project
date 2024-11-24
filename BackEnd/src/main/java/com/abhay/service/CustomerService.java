package com.abhay.service;

import com.abhay.dto.*;
import com.abhay.entity.Bill;
import com.abhay.entity.Customer;
import com.abhay.entity.StudentPayment;
import com.abhay.exception.CustomerNotFoundException;
import com.abhay.exception.BillNotFoundException;
import com.abhay.helper.EncryptionService;
import com.abhay.helper.JWTHelper;
import com.abhay.mapper.CustomerMapper;
import com.abhay.repo.BillRepo;
import com.abhay.repo.CustomerRepo;
import com.abhay.repo.StudentPaymentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.*;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepo customerRepo;
    private final CustomerMapper customerMapper;
    private final EncryptionService encryptionService;
    private final BillRepo billRepo;
    private final StudentPaymentRepo studentPaymentRepo;
    /// added
    private BCryptPasswordEncoder passwordEncoder;
    private final JWTHelper jwtHelper;
    public ResponseEntity<Object> createCustomer(CustomerRequest request) {
        try {
            // Convert the request to a Customer entity
            Customer customer = customerMapper.toCustomer(request);

            // Encrypt the password
            customer.setPassword(encryptionService.encode(customer.getPassword()));

            // Save the customer to the repository
            customerRepo.save(customer);

            // Return success response
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", "success",
                            "message", "Customer Created Successfully",
                            "customerId", customer.getEmail()
                    ));
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violations, such as duplicate email
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "status", "error",
                            "message", "Email ID or RollNo already exists"
                    ));
        } catch (IllegalArgumentException e) {
            // Handle invalid arguments, such as null or improperly formatted fields
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", "error",
                            "message", "Invalid input: " + e.getMessage()
                    ));
        } catch (Exception e) {
            // Handle any unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "An unexpected error occurred. Please try again later."
                    ));
        }
    }

    public Customer getCustomer(String email) {
        return customerRepo.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(
                        format("Cannot update Customer:: No customer found with the provided ID:: %s", email)
                ));
    }

    public CustomerResponse retrieveCustomer(String email) {
        Customer customer = getCustomer(email);
        return customerMapper.toCustomerResponse(customer);
    }

    public  ResponseEntity<Map<String, Object>> login(LoginRequest request) {
        try {
            // Fetch the customer by email
            Customer customer = getCustomer(request.email());

            // Validate the password
            if (!encryptionService.validates(request.password(), customer.getPassword())) {
                return generateResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password", null);
            }

            // Generate the JWT token
            String token = jwtHelper.generateToken(customer);

            // Prepare the response map
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", "success");
            responseBody.put("message", "Login successful");
            responseBody.put("token", token);

            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch (Exception e) {
            // Handle unexpected exceptions
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "An error occurred during login. Please try again.");
            errorResponse.put("details", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<UnpaidBills> GetunpaidBills(Long studentID) {
        List<Object[]> results = customerRepo.findUnpaidBillsByStudentId(studentID);

        List<UnpaidBills> unpaidBills = new ArrayList<>();

        for (Object[] row : results) {
            Long studentId = (Long) row[0];
            String firstName = (String) row[1];
            String lastName = (String) row[2];
            Long billId = (Long) row[3];
            String description = (String) row[4];
            BigDecimal amount = (BigDecimal) row[5];
            LocalDate deadline = ((java.sql.Date) row[6]).toLocalDate();
            BigDecimal totalPaid = (BigDecimal) row[7];
            BigDecimal remainingBalance = (BigDecimal) row[8];

            UnpaidBills bill = new UnpaidBills(studentId, firstName, lastName, billId,
                    description, amount, deadline, totalPaid, remainingBalance);
            unpaidBills.add(bill);
        }

        return unpaidBills;
    }

    public List<PaidBills> GetPaidBills(Long studentID) {
        List<Object[]> results = customerRepo.findpaidBillsByStudentId(studentID);

        List<PaidBills> paidBills = new ArrayList<>();

        for (Object[] row : results) {
            System.out.println(Arrays.toString(row));
            Long studentId = (Long) row[0];
            BigDecimal amount = (BigDecimal) row[1];
            LocalDate paymentDate = ((java.sql.Date) row[2]).toLocalDate();
            Long billId = (Long) row[3];
            BigDecimal remainingBalance = (BigDecimal) row[4];
            String description = (String) row[5];
            // Add the created bill to the list

            PaidBills bill = new PaidBills(studentId,billId,description,amount,paymentDate,remainingBalance);
            paidBills.add(bill);
        }

        return paidBills;
    }
    public List<BillWithPayments> getBillsWithPayments(long studentId) {
        List<Object[]> results = customerRepo.findBillsWithPaymentsByStudentId(studentId);

        Map<Long, BillWithPayments> billMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            Long billId = (Long) row[0];
            String description = (String) row[1];
            BigDecimal amount = (BigDecimal) row[2];
            String billDate = (String) row[3];
            String deadline = (String) row[4];
            BigDecimal remaining = (BigDecimal) row[5];
            BigDecimal paid = (BigDecimal) row[6];
            Long paymentId = (Long) row[7];
            BigDecimal paymentAmount = (BigDecimal) row[8];
            String paymentDate = (String) row[9];
            String paymentDescription = (String) row[10];

            // Retrieve or create a new BillWithPayments object
            BillWithPayments bill = billMap.getOrDefault(billId, new BillWithPayments());

            // If this is the first time processing this bill, initialize its fields
            if (!billMap.containsKey(billId)) {
                bill.setBillId(billId);
                bill.setDescription(description);
                bill.setAmount(amount);
                bill.setBillDate(billDate);
                bill.setDeadline(deadline);
                bill.setRemaining(amount); // Start with the full bill amount
                bill.setPaid(BigDecimal.ZERO); // Start with zero paid
            }

            // Adjust the remaining and paid amounts
            BigDecimal currentRemaining = bill.getRemaining().subtract(paymentAmount != null ? paymentAmount : BigDecimal.ZERO);
            BigDecimal currentPaid = bill.getPaid().add(paymentAmount != null ? paymentAmount : BigDecimal.ZERO);
            bill.setRemaining(currentRemaining);
            bill.setPaid(currentPaid);

            // Create a payment detail and add it to the bill
            if (paymentId != null) { // Only add payment details if there is a payment
                BillWithPayments.PaymentDetail payment = new BillWithPayments.PaymentDetail();
                payment.setPaymentId(paymentId);
                payment.setAmount(paymentAmount);
                payment.setPaymentDate(paymentDate);
                payment.setDescription(paymentDescription);

                if (bill.getParts() == null) {
                    bill.setParts(new ArrayList<>()); // Initialize the list if null
                }
                bill.getParts().add(payment);
            }

            // Put the updated bill back into the map
            billMap.put(billId, bill);
        }

        return new ArrayList<>(billMap.values());
    }


    public ResponseEntity<Map<String, Object>> generateResponse(HttpStatus status, String message, String token) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", message);
        if (token != null) {
            response.put("token", token);
        }
        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<Object> payFees(FeesPayment feesPayment) {
        try {
            // Retrieve student and bill based on provided IDs
            Customer student = customerRepo.findById(feesPayment.studentId())
                    .orElseThrow(() -> new CustomerNotFoundException(
                            format("Cannot update Fees:: No customer found with the provided ID:: %d", feesPayment.studentId())
                    ));
            Bill bill = billRepo.findById(feesPayment.billId())
                    .orElseThrow(() -> new BillNotFoundException(
                            format("Cannot update Fees:: No Bill found with the provided ID:: %d", feesPayment.studentId())
                    ));


            // Map the DTO to the entity
            StudentPayment studentPayment = customerMapper.toEntity(feesPayment, student, bill);
            System.out.println(studentPayment.toString());
            // Save the payment
            studentPaymentRepo.save(studentPayment);

            // Return success response
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "status", "success",
                            "message", "Fees Paid Successfully"
                    ));
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violations, such as duplicate email
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "status", "error",
                            "message", "Fees already Paid"
                    ));
        } catch (IllegalArgumentException e) {
            // Handle invalid arguments, such as null or improperly formatted fields
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "status", "error",
                            "message", "Invalid input: " + e.getMessage()
                    ));
        } catch (Exception e) {
            // Handle any unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "An unexpected error occurred. Please try again later."
                    ));
        }
    }
}
