package com.mi.manchi.telegram.controller;

import com.mi.manchi.telegram.config.GroupMessageBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@RestController
@RequestMapping("/sendMessage")
@RequiredArgsConstructor
public class SendMessageController {

	private final GroupMessageBot groupMessageBot;

	@GetMapping("/sendTxt")
	public String sendMessage(String chatId, String text) throws TelegramApiException {
		SendMessage message = new SendMessage();
		message.setChatId(chatId);
		message.setText(text);
		groupMessageBot.execute(message);
		return "success";
	}

	@GetMapping("/deleteMessage")
	public String deleteMessage(String chatId, Integer messageId) {
		DeleteMessage deleteMsg = new DeleteMessage();
		deleteMsg.setChatId(chatId);
		deleteMsg.setMessageId(messageId);
		try {
			groupMessageBot.execute(deleteMsg);
		}
		catch (TelegramApiException e) {
			log.error("del spam msg failed：{}", e.getMessage(), e);
		}
		return "success";
	}

}
