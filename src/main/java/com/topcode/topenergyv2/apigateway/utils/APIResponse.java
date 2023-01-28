package com.topcode.topenergyv2.apigateway.utils;

import java.util.HashMap;
import java.util.Map;

public  class APIResponse<T> {
    private T data;
    private boolean success;
    private String message;

    public APIResponse(T data, boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }

    public APIResponse() {
    }


    public Map<String,Object> getAsMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("data",this.data);
        map.put("success",this.success);
        map.put("message",this.message);
        return map;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
