package com.example.mail.model;

import com.google.common.collect.HashBasedTable;
import com.sun.org.apache.xml.internal.utils.Hashtree2Node;

import java.util.HashMap;
import java.util.Hashtable;

public class ResultModel extends Hashtable<String, Object> {

	private static final long serialVersionUID = 1L;

	public ResultModel() {
		put("code", 200);
	}

	public static ResultModel ok() {
		return ok(200, "成功", "");
	}

	public static ResultModel ok(Object data) {
		return ok(200, "成功", data);
	}

	public static ResultModel ok(int code, Object msg, Object data) {
		ResultModel result = new ResultModel();
		result.put("code", code);
		result.put("msg", msg);
		result.put("data", data);
		return result;
	}

	public static ResultModel error() {
		return error(500, "未知异常，请联系管理员");
	}

	public static ResultModel error(String msg) {
		return error(500, msg);
	}

	public static ResultModel error(int code, String msg) {
		ResultModel r = new ResultModel();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	@Override
	public ResultModel put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}