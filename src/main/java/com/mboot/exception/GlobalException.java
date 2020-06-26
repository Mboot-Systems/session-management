package com.mboot.exception;


import com.mboot.result.CodeMsg;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
public class GlobalException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private CodeMsg cm;
	
	public GlobalException(CodeMsg cm) {
		super(cm.toString());
		this.cm = cm;
	}

	public CodeMsg getCm() {
		return cm;
	}

}
