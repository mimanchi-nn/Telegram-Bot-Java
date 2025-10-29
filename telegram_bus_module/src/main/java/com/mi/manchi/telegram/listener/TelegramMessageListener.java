package com.mi.manchi.telegram.listener;

import com.mi.manchi.telegram.busservice.BusBotAdminService;
import com.mi.manchi.telegram.model.event.TelegramMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Service
@Slf4j
public class TelegramMessageListener {

	private final BusBotAdminService busBotAdminService;

	@Async
	@EventListener(TelegramMessageEvent.class)
	public void messageDispatch(TelegramMessageEvent event) {
		Update message = event.getMessage();
		// 处理机器人被邀请信息
		if (message.hasMyChatMember()) {
			busBotAdminService.processBotJoinGroup(message.getMyChatMember());
		}
		// 处理用户被邀请入群事件
		if (message.hasChatMember()) {
			busBotAdminService.processUserInvitedToGroup(message.getChatMember());
		}
		// 处理邀请链接的回调信息
		if (message.hasCallbackQuery()) {
			busBotAdminService.parseInviteLinkCallback(message.getCallbackQuery());
		}
		// 处理文本消息
		if (message.hasMessage() && message.getMessage().hasText()) {
			busBotAdminService.processGroupMessage(message.getMessage());
		}
	}

}
