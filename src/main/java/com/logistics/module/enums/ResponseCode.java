package com.logistics.module.enums;

public enum ResponseCode {
    SUCCESS(1, "成功"),
    FAILED(0, "失败"),

    WRITE_ERROR_MSG(-9999,"")
    ;
    private int code;

    private String msg;

    private ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

