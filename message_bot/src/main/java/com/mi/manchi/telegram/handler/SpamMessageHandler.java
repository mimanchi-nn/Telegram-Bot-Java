package com.mi.manchi.telegram.handler;

import com.mi.manchi.telegram.config.GroupMessageBot;
import com.mi.manchi.telegram.entity.MessageDTO;
import com.mi.manchi.telegram.utils.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

/**
 * 积分查询指令处理器：处理 /point、/积分 等指令
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpamMessageHandler implements MessageHandler {

	private final List<String> spamKeywords = Arrays.asList("广告", "兼职", "刷单", "二维码", "全网最低");

	@Override
	public String[] getSupportedCommands() {
		return new String[0];
	}

	@Override
	public SendMessage handle(MessageDTO data) {
		String text = data.getMessageInfo().getContent().trim().toLowerCase();
		if (spamKeywords.stream().anyMatch(text::contains)) {
			delMessage(data.getMessageInfo().getGroupId(), data.getMessageInfo().getMessageId());
		}
		return null;
	}

	@Override
	public Boolean isAdmin() {
		return false;
	}

	@Override
	public Boolean delete() {
		return false;
	}

	private void delMessage(Long chatId, Integer messageId) {
		DeleteMessage deleteMsg = new DeleteMessage();
		deleteMsg.setChatId(chatId.toString());
		deleteMsg.setMessageId(messageId);
		try {
			GroupMessageBot bean = SpringUtil.getBean(GroupMessageBot.class);
			bean.execute(deleteMsg);
		}
		catch (TelegramApiException e) {
			log.error("del spam msg failed：{}", e.getMessage(), e);
		}
	}

}
