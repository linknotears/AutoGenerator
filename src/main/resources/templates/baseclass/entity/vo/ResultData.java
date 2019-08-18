package ${package.Entity}.vo;

import java.util.HashMap;
import java.util.Map;

public class ResultData {
	/**
	 * 0代表操作没有问题，-1代表操作失败
	 */
	private int code = 0;
	
	private Map<String,Object> data = new HashMap<String, Object>();
	//提示消息
	private String message = null;
	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getCode() {
		return code;
	}

	public ResultData setCode(int code) {
		this.code = code;
		return this;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public ResultData setData(String key,Object value) {
		data.put(key, value);
		return this;
	}
	
}
