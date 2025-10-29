package com.mi.manchi.telegram.model.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public class TelegramMessageEvent extends ApplicationEvent {

	private final Update message; // 原始消息对象

	public TelegramMessageEvent(Object source, Update message) {
		super(source);
		this.message = message;
	}

}
