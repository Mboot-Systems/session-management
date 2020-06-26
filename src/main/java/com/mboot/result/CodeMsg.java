package com.mboot.result;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
public class CodeMsg {

	private int code;
	private String msg;

	// Common error codes
	public static CodeMsg SUCCESS = new CodeMsg(200, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "Server exception");
	public static CodeMsg BIND_ERROR = new CodeMsg(500101, "Parameter check exception: %s");
	public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "Illegal request");
	public static CodeMsg ACCESS_LIMIT_REACHED = new CodeMsg(500103, "Visit too often!");

	public static CodeMsg USER_NOT_LOGIN = new CodeMsg(500200, "User not logged in");
	public static CodeMsg TOKEN_INVALID = new CodeMsg(500201, "token is invalid");
	public static CodeMsg USERNAME_NOT_EXIST = new CodeMsg(500202, "Username does not exist");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500203, "Password error");
	public static CodeMsg OVER_MAX_USER_IMPORT_LIMIT = new CodeMsg(500204, "Maximum import %s at a time");
	public static CodeMsg IMPORT_FIELD_FORMAT_ERROR = new CodeMsg(500205, "Line %s format error in %s");
	public static CodeMsg IMPORT_FIELD_IS_EAMPTY = new CodeMsg(500206, "Line %s %s cannot be empty");

	private CodeMsg() {
	}

	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
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

	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMsg(code, message);
	}

	@Override
	public String toString() {
		return "CodeMsg [code=" + code + ", msg=" + msg + "]";
	}

}
