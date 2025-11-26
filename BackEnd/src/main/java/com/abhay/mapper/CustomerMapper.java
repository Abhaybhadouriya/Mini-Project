package com.abhay.mapper;

import com.abhay.dto.CustomerRequest;
import com.abhay.dto.CustomerResponse;
import com.abhay.dto.FeesPayment;
import com.abhay.entity.Bill;
import com.abhay.entity.Customer;
import com.abhay.entity.StudentPayment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CustomerMapper {
    public Customer toCustomer(CustomerRequest request) {
        return Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .rollNumber(request.rollNumber())
                .cgpa(request.cgpa() != null ? request.cgpa() : 0.0f)
                .graduationYear(request.graduationYear() != null ? request.graduationYear() : 1900)
                .totalCredits(request.totalCredits() != null ? request.totalCredits() : 0.0f)
                .domain(request.domain())
                .placementId(request.placementId() != null ? request.placementId() : 0)
                .photographPath(request.photographPath())
                .specialisation(request.specialisation())
                .build();

    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(customer.getFirstName(), customer.getLastName(), customer.getEmail());
    }

    public StudentPayment toEntity(FeesPayment feesPayment, Customer student, Bill bill) {
        return StudentPayment.builder()
                .amount(feesPayment.amount())
                .student(student)
                .description(feesPayment.description())
                .bill(bill)
                .paymentDate(LocalDate.now())  // Assuming the payment date is the current date
                .build();
    }
}






//package com.abhay.mapper;
//
//import com.abhay.dto.CustomerRequest;
//import com.abhay.dto.CustomerResponse;
//import com.abhay.dto.FeesPayment;
//import com.abhay.entity.Bill;
//import com.abhay.entity.Customer;
//import com.abhay.entity.StudentPayment;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//
//@Service
//public class CustomerMapper {
//    public Customer toCustomer(CustomerRequest request) {
//        return Customer.builder()
//                .firstName(request.firstName())
//                .lastName(request.lastName())
//                .email(request.email())
//                .password(request.password())
//                .rollNumber(request.rollNumber()) // Ensures rollNumber is mapped
//                .cgpa(request.cgpa() != null ? request.cgpa() : 0.0f) // Default value for cgpa
//                .graduationYear(request.graduationYear() != null ? request.graduationYear() : 1900) // Default graduationYear
//                .totalCredits(request.totalCredits() != null ? request.totalCredits() : 0.0f) // Default totalCredits
//                .domain(request.domain()) // Nullable, so no default needed
//                .placementId(request.placementId() != null ? request.placementId() : 0) // Default placementId
//                .photographPath(request.photographPath()) // Nullable, no default needed
//                .specialisation(request.specialisation()) // Nullable, no default needed
//                .build();
//
//    }
//
//    public CustomerResponse toCustomerResponse(Customer customer) {
//        return new CustomerResponse(customer.getFirstName(), customer.getLastName(), customer.getEmail());
//    }
//
//    public StudentPayment toEntity(FeesPayment feesPayment, Customer student, Bill bill) {
//        return StudentPayment.builder()
//                .amount(feesPayment.amount())
//                .student(student)
//                .description(feesPayment.description())
//                .bill(bill)
//                .paymentDate(LocalDate.now())  // Assuming the payment date is the current date
//                .build();
//    }
//}
