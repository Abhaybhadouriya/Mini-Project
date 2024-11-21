package com.abhay.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

public class UnpaidBills {

    private Long studentId;
    private String firstName;
    private String lastName;
    private Long billId;
    private String description;
    private BigDecimal amount;
    private LocalDate deadline;
    private BigDecimal totalPaid;
    private BigDecimal remainingBalance;

    // Constructor
    public UnpaidBills(Long studentId, String firstName, String lastName, Long billId, String description,
                         BigDecimal amount, LocalDate deadline, BigDecimal totalPaid, BigDecimal remainingBalance) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.billId = billId;
        this.description = description;
        this.amount = amount;
        this.deadline = deadline;
        this.totalPaid = totalPaid;
        this.remainingBalance = remainingBalance;
    }

    // Getters and Setters
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
}
