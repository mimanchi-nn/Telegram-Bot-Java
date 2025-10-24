package com.mi.manchi.telegram.handler;

import com.mi.manchi.telegram.entity.MessageDTO;
import com.mi.manchi.telegram.entity.MessageInfo;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * 指令处理器接口：所有具体指令处理器需实现此接口
 */
public interface MessageHandler {

	/**
	 * 获取当前处理器支持的指令（如 "/checkin"、"/签到"）
	 * @return 指令列表（支持多个别名）
	 */
	String[] getSupportedCommands();

	/**
	 * 处理指令逻辑
	 * @param data 消息对象
	 * @return 要发送的回复消息
	 */
	SendMessage handle(MessageDTO data);

	/**
	 * 是否需要管理员权限
	 * @return true 如果指令需要管理员权限，false 则不需要
	 */
	Boolean isAdmin();

	Boolean delete();

}
