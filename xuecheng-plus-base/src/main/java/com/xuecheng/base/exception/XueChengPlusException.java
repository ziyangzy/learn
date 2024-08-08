package com.xuecheng.base.exception;

import lombok.Data;

/**
 * @author Mr.M
 * @version 1.0
 * @description 本项目自定义异常类型
 * @date 2023/2/12 16:56
 */
@Data
public class XueChengPlusException extends RuntimeException {
    private int errCode = 0;
    private String errMessage;

    public XueChengPlusException() {
    }

    public XueChengPlusException(String message) {
        super(message);
        this.errMessage = message;

    }
    
    public XueChengPlusException(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }
    


    public static void cast(String message){
        throw new XueChengPlusException(message);
    }
    public static void cast(int errCode,String message){
        throw new XueChengPlusException(errCode,message);
    }
    public static void cast(CommonError error){
        throw new XueChengPlusException(error.getErrMessage());
    }

}
