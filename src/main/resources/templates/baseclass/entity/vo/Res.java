package ${package.Entity}.vo;

import java.util.HashMap;
import java.util.Map;

public class Res {
	/**
	 * 0代表操作没有问题，-1代表操作失败
	 */
	private int code = 0;
	
	private Map<String,Object> data = new HashMap<String, Object>();
	//提示消息
	private String msg = null;
	
	
	public String getMsg() {
		return msg;
	}

	public Res setMsg(String msg) {
		this.msg = msg;
		return this;
	}
	
	public int getCode() {
		return code;
	}

	public Res setCode(int code) {
		this.code = code;
		return this;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public Res setData(String key,Object value) {
		data.put(key, value);
		return this;
	}
	
}
