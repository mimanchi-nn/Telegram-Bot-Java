package com.mi.manchi.telegram.handler;

import com.mi.manchi.telegram.model.entity.MessageDTO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

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

	/**
	 * 是否删除发送的消息
	 * @return true 如果需要删除，false 则不需要
	 */
	Boolean delete();

}
