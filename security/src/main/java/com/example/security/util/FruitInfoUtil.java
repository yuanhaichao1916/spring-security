package com.example.security.util;

import com.example.security.annot.FruitColor;
import com.example.security.annot.FruitName;
import com.example.security.annot.FruitProvider;

import java.lang.reflect.Field;

/**
 * 注解处理器
 */
public class FruitInfoUtil {
    public static void getFruitInfo(Class<?> clazz){


        String strFruitName = " 水果名称: ";
        String strFruitColor = " 水果颜色: ";
        String strFruitProvider = " 供应商信息: ";

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            if(declaredField.isAnnotationPresent(FruitName.class)){
                FruitName fruitName = declaredField.getAnnotation(FruitName.class);
                String fruitNames = strFruitName + fruitName.value();
                System.out.println(fruitNames);
            } else if (declaredField.isAnnotationPresent(FruitColor.class)) {
                FruitColor fruitColor = declaredField.getAnnotation(FruitColor.class);
                String fruitColors = strFruitColor + fruitColor.fruitColor().toString();
                System.out.println(fruitColors);
            } else if (declaredField.isAnnotationPresent(FruitProvider.class)) {
                FruitProvider fruitProvider = declaredField.getAnnotation(FruitProvider.class);
                String fruitProviders = strFruitProvider + "供应商编号:" + fruitProvider.id() + " 供应商名称:" + fruitProvider.name() + " 供应商地址:" + fruitProvider.address();
                System.out.println(fruitProviders);
            }
        }
    }

}
