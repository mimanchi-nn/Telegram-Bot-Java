package com.mi.manchi.telegram.service;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TelegramBotService {

	/**
	 * 判断用户是否是管理员
	 * @param chatId
	 * @param memberId
	 * @return
	 */
	Boolean isAdmin(Long chatId, Long memberId);

	/**
	 * 删除消息
	 * @param message
	 */
	void deleteMessage(DeleteMessage message);

	/**
	 * 发送消息
	 * @param message
	 */
	Message sendMessage(BotApiMethodMessage message);

	/**
	 * 定时删除消息
	 * @param chatId
	 * @param messageId
	 */
	void scheduleMsgDelete(Long chatId, Integer messageId);

}
