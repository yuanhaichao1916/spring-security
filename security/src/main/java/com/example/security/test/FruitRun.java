package com.example.security.test;

import com.example.security.entry.Apple;
import com.example.security.util.FruitInfoUtil;

public class FruitRun {
    public static void main(String[] args) {
        FruitInfoUtil.getFruitInfo(Apple.class);
    }
}
