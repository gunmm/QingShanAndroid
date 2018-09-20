package com.qsjt.qingshan.model;

/**
 * @author LiYouGui
 */

public class BasicResponse<T> {

    private String result_code;

    private String reason;

    private T object;

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult() {
        return reason;
    }

    public void setResult(String result) {
        this.reason = result;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
