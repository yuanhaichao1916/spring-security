package com.example.security.annot;

import java.lang.annotation.*;

/**
 * 水果名称
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitName {
    String value() default "";
}
