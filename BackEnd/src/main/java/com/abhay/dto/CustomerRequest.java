package com.abhay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record CustomerRequest(

        @JsonProperty("cgpa")
        @DecimalMin(value = "0.0", message = "CGPA cannot be less than 0.0")
        @DecimalMax(value = "10.0", message = "CGPA cannot be more than 10.0")
        Float cgpa,

        @JsonProperty("domain")
        String domain,

        @JsonProperty("email")
        @NotNull(message = "Email is required")
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email must be in correct format")
        String email,

        @JsonProperty("first_name")
        @NotNull(message = "First name is required")
        @NotEmpty(message = "First name cannot be empty")
        @NotBlank(message = "First name cannot be blank")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("graduation_year")
        @Min(value = 1900, message = "Graduation year cannot be before 1900")
        @Max(value = 2100, message = "Graduation year cannot be after 2100")
        Integer graduationYear,

        @JsonProperty("password")
        @NotNull(message = "Password is required")
        @NotEmpty(message = "Password cannot be empty")
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 12, message = "Password must be between 6 and 12 characters")
        String password,

        @JsonProperty("photograph_path")
        String photographPath,

        @JsonProperty("placement_id")
        Integer placementId,

        @JsonProperty("roll_number")
        @NotNull(message = "Roll number is required")
        @NotEmpty(message = "Roll number cannot be empty")
        String rollNumber,

        @JsonProperty("specialisation")
        String specialisation,

        @JsonProperty("total_credits")
        @DecimalMin(value = "0.0", message = "Total credits cannot be less than 0.0")
        @DecimalMax(value = "4.0", message = "Total credits cannot be more than 4.0")
        Float totalCredits

) {}
