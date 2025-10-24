package com.mi.manchi.telegram.config;

import com.mi.manchi.telegram.group.GroupMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {

	@Value("${telegram.bot.token}")
	private String botToken;

	@Value("${telegram.bot.username}")
	private String botUsername;

	private final GroupMessageService groupMessageService;

	@Bean
	public GroupMessageBot groupMessageBot() throws TelegramApiException {
		GroupMessageBot bot = new GroupMessageBot(botToken, botUsername, groupMessageService);

		// 注册机器人到 TelegramBotsApi
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(bot);
		return bot;
	}

}
