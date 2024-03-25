package com.concentrate.gpt.gptflow.vo;

/**
 * 
 * @author coderistrator
 *
 */
public class ResponseResult {

	public static final String SUCCESS = "success";
	public static final String FAILED = "failed";
	public static final String EXIESTS = "exists";

	private String status;
	
	private Object obj;

	private Object msg;

	public ResponseResult(String status, Object obj) {
		this(status,obj,null);
	}

	public ResponseResult(String status, Object obj,Object msg) {
		this.status = status;
		this.obj = obj;
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	
}
