package com.example.shopapp.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class ErrorResponse {
    private Date timestamp;
    private String message;
    private int status;
    private String error;
    private String path;
}
