package com.lysf.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lysf.enums.ResponseCode;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class Result<T> implements Serializable{
    private int status;
    private String msg;
    private T data;

    public Result(int status) {
        this.status = status;
    }

    public Result(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Result(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public Result(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    @JsonIgnore
    public boolean isSuccess()
    {
        return this.status== ResponseCode.SUCCESS.getCode();
    }
    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static <T> Result <T> createSuccess(){
        return new Result<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> Result <T> createSuccessMessage(String msg)
    {
        return new Result<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> Result <T> createSuccess(T data)
    {
        return new Result<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> Result <T> createSuccess(String msg,T data)
    {
        return new Result<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public static <T>Result<T> createError()
    {
        return new Result<T>(ResponseCode.ERROR.getCode());
    }
    public static <T> Result <T> createErrorMessage(String errorMessage)
    {
        return new Result<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }
    public static <T> Result <T> createErrorCodeMessage(int errorCode,String errorDesc)
    {
        return new Result<T>(errorCode,errorDesc);
    }

}