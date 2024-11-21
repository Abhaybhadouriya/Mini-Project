package com.abhay.repo;

import com.abhay.entity.StudentPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPaymentRepo extends JpaRepository<StudentPayment, Integer> {
}
