package com.jds.rp.service.vo;

public class RedPacketRecieveVo {
	private boolean get;
	private String error;
	private int type;
	private int num;
	private int numRec;
	private int amt;
	private int uid;
	private String nickName;
	private String img;
	
	public boolean isGet() {
		return get;
	}
	public void setGet(boolean get) {
		this.get = get;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public int getNumRec() {
		return numRec;
	}
	public void setNumRec(int numRec) {
		this.numRec = numRec;
	}
	public int getAmt() {
		return amt;
	}
	public void setAmt(int amt) {
		this.amt = amt;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
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
