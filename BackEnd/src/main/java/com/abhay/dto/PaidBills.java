package com.abhay.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaidBills {

    private Long studentId;
    private Long billId;
    private String description;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private BigDecimal remainingBalance;

    // Constructor
    public PaidBills(Long studentId,  Long billId, String description,
                       BigDecimal amount, LocalDate paymentDate,  BigDecimal remainingBalance) {
        this.studentId = studentId;

        this.billId = billId;
        this.description = description;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.remainingBalance = remainingBalance;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }


    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDeadline() {
        return paymentDate;
    }

    public void setDeadline(LocalDate deadline) {
        this.paymentDate = deadline;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
}
