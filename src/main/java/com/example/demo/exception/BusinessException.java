package com.example.demo.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) { super(message); } // Separar validaciones de negocio del objeto dto en si.
}


