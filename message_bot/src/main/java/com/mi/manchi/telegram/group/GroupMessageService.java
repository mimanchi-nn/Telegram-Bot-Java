package com.mi.manchi.telegram.group;

import com.mi.manchi.telegram.busservice.BusBotAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Service
@Slf4j
public class GroupMessageService {

	private final BusBotAdminService busBotAdminService;

	public void process(Update update) {
		// 处理机器人被邀请信息
		if (update.hasMyChatMember()) {
			busBotAdminService.processBotJoinGroup(update.getMyChatMember());
		}
		// 处理用户被邀请入群事件
		if (update.hasChatMember()) {
			busBotAdminService.processUserInvitedToGroup(update.getChatMember());
		}
		// 处理邀请链接的回调信息
		if (update.hasCallbackQuery()) {
			busBotAdminService.parseInviteLinkCallback(update.getCallbackQuery());
		}
		// 处理文本消息
		if (update.hasMessage() && update.getMessage().hasText()) {
			busBotAdminService.processGroupMessage(update.getMessage());
		}
	}

}
