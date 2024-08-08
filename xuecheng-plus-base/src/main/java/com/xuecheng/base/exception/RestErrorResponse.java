package com.xuecheng.base.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Mr.M
 * @version 1.0
 * @description 和前端约定返回的异常信息模型
 * @date 2023/2/12 16:55
 */
@Data
public class RestErrorResponse implements Serializable {

 private int errCode;
 private String errMessage;

 public RestErrorResponse(String errMessage){
  this.errMessage= errMessage;
 }
 public RestErrorResponse(int errCode,String errMessage){
  this.errCode= errCode;
  this.errMessage= errMessage;
 }

}
