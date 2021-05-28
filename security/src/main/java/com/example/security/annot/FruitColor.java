package com.example.security.annot;

import java.lang.annotation.*;

/**
 * 水果颜色
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FruitColor {
    // 颜色枚举
    public enum Color{BLUE,RED,GREEN};

    // 颜色属性
    Color fruitColor() default Color.GREEN;
}
