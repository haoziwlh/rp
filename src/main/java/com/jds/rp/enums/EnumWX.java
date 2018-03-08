package com.jds.rp.enums;

public enum EnumWX {

	CODE_SESSION("sns/jscode2session"),
	;
	
	private String url;
	private EnumWX(String url) {
		this.url = url;
	}
	
	public String url() {
		return this.url;
	}
	
	public String parsedUrl() {
		return "https://api.weixin.qq.com/" + this.url;
	}
	
}
