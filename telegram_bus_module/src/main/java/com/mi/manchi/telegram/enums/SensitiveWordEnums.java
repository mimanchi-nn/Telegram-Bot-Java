package com.mi.manchi.telegram.enums;

import lombok.Data;
import lombok.Getter;

@Data
public class SensitiveWordEnums {

	/**
	 * 敏感词处理方式
	 */
	@Getter
	public enum ProcessType {

		/**
		 * 仅删除
		 */
		DELETED(0),
		/**
		 * 不处理
		 */
		DEFAULT(1),
		/**
		 * 删除并且警告
		 */
		DELETED_WARN(2),
		/**
		 * 封禁用户
		 */
		BAN(3);

		;

		private final int code;

		ProcessType(int code) {
			this.code = code;
		}

	}

}
