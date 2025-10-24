package com.mi.manchi.telegram.busservice;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BusBotAdminService {

	/**
	 * 处理机器人加入群聊事件
	 * @param data 事件数据
	 */
	void processBotJoinGroup(ChatMemberUpdated data);

	/**
	 * 处理用户被邀请入群事件
	 * @param data 事件数据
	 */
	void processUserInvitedToGroup(ChatMemberUpdated data);

	/**
	 * 解析邀请链接的回调信息
	 * @param data
	 */
	void parseInviteLinkCallback(CallbackQuery data);

	/**
	 * 处理群聊消息事件
	 * @param message 事件数据
	 */
	void processGroupMessage(Message message);

}
