package com.abhay.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BillNotFoundException extends RuntimeException {
    private final String msg;
}
