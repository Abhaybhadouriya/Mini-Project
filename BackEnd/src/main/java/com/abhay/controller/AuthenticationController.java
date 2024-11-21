package com.abhay.controller;

import com.abhay.dto.LoginRequest;
import com.abhay.helper.RequestInterceptor;
import com.abhay.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Authorization")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final CustomerService customerService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Valid LoginRequest request) {
        return customerService.login(request);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Authorization")
    @PostMapping("/checkValidation")
    public ResponseEntity<Map<String, Object>> checkValidation() {
        return customerService.generateResponse(HttpStatus.OK,"Success : Authorized Request",null);
    }
}
