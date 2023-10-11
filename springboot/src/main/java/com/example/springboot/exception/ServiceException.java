package com.example.springboot.exception;

import lombok.Getter;

/**
 * @author Ji_ah
 * 2023-10-10 15:47
 */
@Getter
public class ServiceException extends RuntimeException {

    private final String code;

    public ServiceException(String msg) {
        super(msg);
        this.code = "500";
    }

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

}
