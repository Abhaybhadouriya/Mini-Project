package com.abhay.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BillWithPayments {
    private Long billId;
    private String description;
    private BigDecimal amount;
    private String billDate;
    private String deadline;
    private BigDecimal remaining;
    private BigDecimal paid;
    private List<PaymentDetail> parts = new ArrayList<>(); // Initialize here

    public static class PaymentDetail {
        private Long paymentId;
        private BigDecimal amount;
        private String paymentDate;
        private String description;

        // Getters and Setters
        public Long getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(Long paymentId) {
            this.paymentId = paymentId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    // The rest of your getters and setters
}
