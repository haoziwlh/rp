package com.jds.rp.dao.dto;

import java.util.Date;

public class User {
	private int id;
	private String openid;
	private int amt;
	private int frozenAmt;
	private String nickName;
	private String img;
	private Date bindTime;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public int getAmt() {
		return amt;
	}
	public void setAmt(int amt) {
		this.amt = amt;
	}
	public int getFrozenAmt() {
		return frozenAmt;
	}
	public void setFrozenAmt(int frozenAmt) {
		this.frozenAmt = frozenAmt;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Date getBindTime() {
		return bindTime;
	}
	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
	}
}
