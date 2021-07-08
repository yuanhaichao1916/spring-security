package com.example.security.enums;

public enum ResultEnum {
    LOGIN_ERROR(1,"LOGIN_ERROR"),
    CAPTCHA_ERROR(2,"CAPTCHA_ERROR");

    private Integer code;
    private String type;

    ResultEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
