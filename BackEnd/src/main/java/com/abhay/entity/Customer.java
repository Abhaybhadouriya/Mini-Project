package com.abhay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Students")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="roll_number",unique = true,nullable = false)
    private String rollNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name = "photograph_path")
    private String photographPath;

    @Column(name="cgpa")
    private float cgpa;

    @Column(name="total_credits")
    private float totalCredits;

    @Column(name="graduation_year")
    private int graduationYear;

    @Column(name="domain" , nullable = true)
    private String domain;

    @Column(name="specialisation" ,nullable = true)
    private String specialisation;

    @Column(name="placement_id",nullable = true)
    private int placementId;
}
