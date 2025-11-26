package com.abhay.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
// FIX: Removed @Data, which incorrectly generated a no-args constructor
public class CustomerNotFoundException extends RuntimeException {
    private final String msg;

    // FIX: Manual constructor to accept the String message used in CustomerService
    public CustomerNotFoundException(String msg) {
        super(msg);
        this.msg = msg;
    }
}