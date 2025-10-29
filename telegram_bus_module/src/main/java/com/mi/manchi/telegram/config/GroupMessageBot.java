package com.mi.manchi.telegram.config;

import com.mi.manchi.telegram.dispatch.TelegramMessageListener;
import com.mi.manchi.telegram.model.event.TelegramMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@RequiredArgsConstructor
public class GroupMessageBot extends TelegramLongPollingBot {

	private final String botToken;

	private final String botUsername;

	private final ApplicationEventPublisher eventPublisher; // 事件发布器

	@Override
	public String getBotToken() {
		return botToken;
	}

	@Override
	public void onUpdateReceived(Update update) {
		log.info("Received update: {}", update);
		eventPublisher.publishEvent(new TelegramMessageEvent(this, update));

	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

}
