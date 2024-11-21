package com.abhay.controller;

import com.abhay.dto.*;
import com.abhay.repo.CustomerRepo;
import com.abhay.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Authorization")
@RequestMapping("api/students")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepo customerRepo;


    @GetMapping("/{email}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable("email") String email) {
        return ResponseEntity.ok(customerService.retrieveCustomer(email));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> createCustomer(@RequestBody @Valid CustomerRequest request, BindingResult bindingResult) {
        // Check if there are any validation errors
        if (bindingResult.hasErrors()) {
            // Handling validation errors manually
            StringBuilder errors = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("; "));
            System.out.println(errors.toString());
            return ResponseEntity
                    .badRequest() // 400 Bad Request
                    .body(Map.of("status", "error", "message", errors.toString()));
        }

        try {
            // Proceed with creating the customer if no validation errors
           return customerService.createCustomer(request);

        } catch (Exception e) {
            // Handle other errors (e.g., database issues)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Success");
    }



    // Allow requests from localhost:3000 only
    @PostMapping("/unpaidbills")
    public ResponseEntity<List<UnpaidBills>> getUnpaidBills(@RequestBody StudentBillViewRequestDTO studentRequest) {
        List<UnpaidBills> unpaidBills = customerService.GetunpaidBills(studentRequest.getStudentId());
        return ResponseEntity.ok(unpaidBills);
    }
    @PostMapping("/paidbills")
    public ResponseEntity<List<PaidBills>> getPaidBills(@RequestBody StudentBillViewRequestDTO studentRequest) {
        List<PaidBills> paidBills= customerService.GetPaidBills(studentRequest.getStudentId());
        return ResponseEntity.ok(paidBills);
    }
    @PostMapping("/viewbills")
    public ResponseEntity<List<BillWithPayments>> getAllBills(@RequestBody StudentBillViewRequestDTO studentRequest) {
        List<BillWithPayments> paidBills= customerService.getBillsWithPayments(studentRequest.getStudentId());
        return ResponseEntity.ok(paidBills);
    }

    @PostMapping("/payFees")
    public ResponseEntity<Object> payFees(@RequestBody @Valid FeesPayment feesPayment) {
        return customerService.payFees(feesPayment);
    }

}
