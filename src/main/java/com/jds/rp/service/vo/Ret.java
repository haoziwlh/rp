package com.jds.rp.service.vo;

public class Ret {
	private int code;
	private String error;
	private Object data;
	
	
	public Ret(int code, String error) {
		super();
		this.code = code;
		this.error = error;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public static Ret buildOk() {
		return new Ret(0, "成功");
	}
	
	public static Ret buildError(String error) {
		return new Ret(-1, error);
	}
	
}
