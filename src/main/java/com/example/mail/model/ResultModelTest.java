package com.example.mail.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Hashtable;

@ApiModel(value = "出参")
public class ResultModelTest<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "状态码", notes = "aabb")
	private int code;

	@ApiModelProperty(value = "描述")
	private String msg;

	@ApiModelProperty(value = "数据")
	private T data;

	public ResultModelTest() {

	}

	public ResultModelTest(int code, String msg) {
		this(code, msg, null);
	}

	public ResultModelTest(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ResultModelTest<T> OK(T data) {
		return new ResultModelTest(200, "成功", data);
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}