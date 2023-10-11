package com.example.springboot.common;

import java.lang.annotation.*;

/**
 * @author Ji_ah
 * 2023-10-11 16:13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {
}
