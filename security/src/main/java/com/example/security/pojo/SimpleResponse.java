package com.example.security.pojo;

import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class SimpleResponse implements Serializable {
    private int code;
    private Object data;

    public static SimpleResponse success() {
        return new SimpleResponse(HttpStatus.OK, "Operation complete!");
    }

    public static SimpleResponse success(Object data) {
        return new SimpleResponse(HttpStatus.OK, data);
    }

    public static SimpleResponse error(Object data) {
        return new SimpleResponse(HttpStatus.INTERNAL_SERVER_ERROR, data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public SimpleResponse(HttpStatus code, Object data) {
        this.code = code.value();
        this.data = data;
    }

    public SimpleResponse(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public String toJSon() {
        return new GsonBuilder().create().toJson(this);
    }

    public HttpStatus createHttpStatus() {
        return HttpStatus.resolve(code);
    }
}