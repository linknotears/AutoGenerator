package com.linkey.entity.vo;

import java.util.HashMap;
import java.util.Map;

public class ResultData {
	/**
	 * 0代表操作没有问题，-1代表操作失败
	 */
	private int code = 0;
	
	private Map<String,Object> data = new HashMap<String, Object>();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(String key,Object value) {
		data.put(key, value);
	}
	
	
	
}
