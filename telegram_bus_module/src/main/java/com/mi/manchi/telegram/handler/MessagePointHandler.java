package com.mi.manchi.telegram.handler;

import com.mi.manchi.telegram.busservice.BusPointService;
import com.mi.manchi.telegram.model.entity.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessagePointHandler implements MessageHandler {

	private final BusPointService busPointService;

	@Override
	public String[] getSupportedCommands() {
		return new String[0];
	}

	@Override
	public SendMessage handle(MessageDTO data) {
		busPointService.addPointByMessageInfo(data.getMessageInfo());
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

}
