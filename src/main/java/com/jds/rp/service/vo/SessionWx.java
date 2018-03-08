package com.jds.rp.service.vo;

public class SessionWx {
	private String sessionKey;
	private String openid;
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	@Override
	public String toString() {
		return "SessionWx [sessionKey=" + sessionKey + ", openid=" + openid + "]";
	}
	
}
