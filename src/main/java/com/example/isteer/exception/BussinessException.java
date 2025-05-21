package com.example.isteer.exception;

import com.example.isteer.enums.CVSSEnum;

public class BussinessException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	 private final CVSSEnum error;
		public BussinessException(CVSSEnum IdException) {
			super(IdException.getStatusMessage());
			   this.error = IdException;
		}
		
		
		public  CVSSEnum getError() {
			return error;
		}

}
