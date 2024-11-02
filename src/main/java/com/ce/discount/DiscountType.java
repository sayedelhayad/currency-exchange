package com.ce.discount;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DiscountType {
    String name();
    int priority() default 1;
}