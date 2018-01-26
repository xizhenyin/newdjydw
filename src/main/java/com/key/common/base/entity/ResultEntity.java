package com.key.common.base.entity;

import java.io.Serializable;

public class ResultEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3416606856177432498L;
//	private boolean success = true;
//	private String errorCode = "200";
//	private String message = "正确";
	private Object data;
//	public boolean isSuccess() {
//		return success;
//	}
//	public void setSuccess(boolean success) {
//		this.success = success;
//	}
//	public String getErrorCode() {
//		return errorCode;
//	}
//	public void setErrorCode(String errorCode) {
//		this.errorCode = errorCode;
//	}
//	public String getMessage() {
//		return message;
//	}
//	public void setMessage(String message) {
//		this.message = message;
//	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
