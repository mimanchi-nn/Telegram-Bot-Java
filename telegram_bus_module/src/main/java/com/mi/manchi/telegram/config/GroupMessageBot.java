package com.mi.manchi.telegram.config;

import com.mi.manchi.telegram.dispatch.GroupMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RequiredArgsConstructor
public class GroupMessageBot extends TelegramLongPollingBot {

	private final String botToken;

	private final String botUsername;

	private final GroupMessageService groupMessageService;

	@Override
	public String getBotToken() {
		return botToken;
	}

	@Override
	public void onUpdateReceived(Update update) {
		log.info("Received update: {}", update);
		groupMessageService.process(update);
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

}
