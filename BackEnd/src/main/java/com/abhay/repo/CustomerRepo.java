package com.abhay.repo;

import com.abhay.dto.UnpaidBills;
import com.abhay.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);

    @Query(value = """
            SELECT 
                s.id AS studentId,
                s.first_name AS firstName,
                s.last_name AS lastName,
                b.id AS bill_Id,
                b.description AS description,
                b.amount AS amount,
                b.deadline AS deadline,
                COALESCE(SUM(sp.amount), 0) AS totalPaid,
                (b.amount - COALESCE(SUM(sp.amount), 0)) AS remainingAmount
            FROM 
                students s
            JOIN 
                student_bills sb ON s.id = sb.student_id
            JOIN 
                bills b ON b.id = sb.bill_id
            LEFT JOIN 
                student_payment sp ON b.id = sp.bill_id AND s.id = sp.student_id
            WHERE 
                s.id = :studentId
            GROUP BY 
                s.id, s.first_name, s.last_name, b.id, b.description, b.amount, b.deadline
            HAVING 
                (b.amount - COALESCE(SUM(sp.amount), 0)) > 0
            LIMIT 25
            """, nativeQuery = true)
    List<Object[]> findUnpaidBillsByStudentId(@Param("studentId") Long studentId);

    @Query(value = """
    SELECT
        sp.id AS paymentId,
        sp.amount,
        sp.payment_date AS paymentDate,
        sb.bill_id AS billId,
        (b.amount - COALESCE(
            (SELECT SUM(sp_inner.amount)
             FROM student_payment sp_inner
             WHERE sp_inner.bill_id = sb.bill_id
               AND sp_inner.student_id = sb.student_id), 0)) AS remainingAmount,
        b.description AS description
    FROM
        student_payment sp
    JOIN
        student_bills sb ON sp.bill_id = sb.bill_id AND sp.student_id = sb.student_id
    JOIN
        bills b ON sb.bill_id = b.id
    WHERE
        sp.student_id = :studentId
    """, nativeQuery = true)
    List<Object[]> findpaidBillsByStudentId(@Param("studentId") long studentId);

    @Query(value = """
   SELECT 
    b.id AS bill_id,
    b.description,
    b.amount AS bill_amount,
    DATE_FORMAT(b.bill_date, '%d/%m/%y') AS bill_date,
    DATE_FORMAT(b.deadline, '%d/%m/%y') AS deadline,
    (b.amount - IFNULL(SUM(sp.amount), 0)) AS remaining, -- Remaining amount for the bill
    IFNULL(SUM(sp.amount), 0) AS total_paid, -- Total paid for the bill
    sp.id AS payment_id,
    sp.amount AS payment_amount,
    DATE_FORMAT(sp.payment_date, '%d/%m/%y') AS payment_date,
    sp.description AS payment_description
FROM 
    bills b
LEFT JOIN 
    student_bills sb ON b.id = sb.bill_id
LEFT JOIN 
    student_payment sp ON sb.bill_id = sp.bill_id AND sb.student_id = sp.student_id
WHERE 
    sb.student_id = :studentId
GROUP BY 
    b.id, sp.id -- Group by bill and payment IDs
ORDER BY 
    b.id, sp.payment_date;
""", nativeQuery = true)
    List<Object[]> findBillsWithPaymentsByStudentId(@Param("studentId") long studentId);

}