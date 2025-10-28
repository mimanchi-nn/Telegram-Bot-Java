package com.mi.manchi.telegram.model.result;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;

	private String message;

	private T data;

	public static <T> R<T> ok() {
		return ok(null);
	}

	public static <T> R<T> ok(T data) {
		return new R<T>().setCode(SystemResultCode.SUCCESS.getCode()).setData(data)
				.setMessage(SystemResultCode.SUCCESS.getMessage());
	}

	public static <T> R<T> ok(T data, String message) {
		return new R<T>().setCode(SystemResultCode.SUCCESS.getCode()).setData(data).setMessage(message);
	}

	public static <T> R<T> failed(int code, String message) {
		return new R<T>().setCode(code).setMessage(message);
	}

}
