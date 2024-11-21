package com.abhay.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record FeesPayment(


            @JsonProperty("bill_id")
            @NotNull(message = "Bill ID is required")
            @Positive(message = "Bill ID must be positive")
            Long billId,

            @JsonProperty("student_id")
            @NotNull(message = "Student ID is required")
            @Positive(message = "Student ID must be positive")
            Long studentId,
            @JsonProperty("amount")
            @NotNull(message = "Amount is required")
            BigDecimal amount,

            @JsonProperty("description")
            String description
) {}