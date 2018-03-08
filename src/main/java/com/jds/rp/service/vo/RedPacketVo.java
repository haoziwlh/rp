package com.jds.rp.service.vo;

import java.util.Date;

public class RedPacketVo {
	private int id;
	private int amt;
	private int curBal;
	private int num;
	private int numRec;
	private int type;
	private int uid;
	private String memo;
	private Date ctime;
	private String nickName;
	private String img;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAmt() {
		return amt;
	}
	public void setAmt(int amt) {
		this.amt = amt;
	}
	public int getCurBal() {
		return curBal;
	}
	public void setCurBal(int curBal) {
		this.curBal = curBal;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getNumRec() {
		return numRec;
	}
	public void setNumRec(int numRec) {
		this.numRec = numRec;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
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
}
