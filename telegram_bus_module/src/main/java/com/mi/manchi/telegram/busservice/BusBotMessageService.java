package com.mi.manchi.telegram.busservice;

import com.mi.manchi.telegram.model.entity.MessageInfo;

public interface BusBotMessageService {

	/**
	 * 处理用户签到事件
	 */
	String doCheckIn(MessageInfo messageInfo);

}
