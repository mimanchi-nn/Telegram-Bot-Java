package com.mi.manchi.telegram.service.impl;

import com.mi.manchi.telegram.config.GroupMessageBot;
import com.mi.manchi.telegram.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramBotServiceImpl implements TelegramBotService {

	private final GroupMessageBot groupMessageBot;

	@Override
	public Boolean isAdmin(Long chatId, Long memberId) {
		GetChatMember getChatMember = new GetChatMember();
		getChatMember.setChatId(chatId);
		getChatMember.setUserId(memberId);
		try {
			ChatMember chatMember = groupMessageBot.execute(getChatMember);
			String status = chatMember.getStatus();
			// 群主（creator）或管理员（administrator）均视为管理员
			return "creator".equals(status) || "administrator".equals(status);
		}
		catch (TelegramApiException e) {
			log.error("check user is admin error, groupId: {}, userId: {}, e: {}", chatId, memberId, e.getMessage(), e);
			return false;
		}
	}

	@Override
	public void deleteMessage(DeleteMessage message) {
		try {
			groupMessageBot.execute(message);
			log.info("已定时删除群 [{}] 的消息,消息ID：{}", message.getChatId(), message.getMessageId());
		}
		catch (TelegramApiException e) {
			log.error("delete message error, chatId: {}, messageId: {}, e: {}", message.getChatId(),
					message.getMessageId(), e.getMessage(), e);
		}
	}

	@Override
	public Message sendMessage(BotApiMethodMessage message) {
		try {
			return groupMessageBot.execute(message);
		}
		catch (Exception e) {
			log.error("send message failed：{}", e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void scheduleMsgDelete(Long chatId, Integer messageId) {
		// 定时任务调度器
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				DeleteMessage deleteMsg = new DeleteMessage();
				deleteMsg.setChatId(chatId);
				deleteMsg.setMessageId(messageId);
				try {
					deleteMessage(deleteMsg);

				}
				finally {
					timer.cancel(); // 任务执行后关闭定时器，避免资源泄漏
				}
			}
		}, 10000); // 延迟DELETE_DELAY毫秒后执行
	}

}
