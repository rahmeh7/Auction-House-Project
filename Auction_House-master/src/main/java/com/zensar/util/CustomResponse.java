package com.zensar.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomResponse {
	private int code;
	private String message;
	private Object object;

	public CustomResponse(int code, String message) {
		this.code = code;
		this.message = message;
	}

}