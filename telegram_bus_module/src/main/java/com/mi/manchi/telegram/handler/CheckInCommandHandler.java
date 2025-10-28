package com.mi.manchi.telegram.handler;

import com.mi.manchi.telegram.busservice.BusBotMessageService;
import com.mi.manchi.telegram.model.entity.MessageDTO;
import com.mi.manchi.telegram.model.entity.MessageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class CheckInCommandHandler implements MessageHandler {

	private final BusBotMessageService busBotMessageService;

	// 支持的指令：/checkin、/签到、/打卡
	@Override
	public String[] getSupportedCommands() {
		return new String[0];
	}

	@Override
	public SendMessage handle(MessageDTO data) {
		MessageInfo messageInfo = data.getMessageInfo();
		String text = messageInfo.getContent();
		if (text.contains("爱懒懒")) {
			String result = busBotMessageService.doCheckIn(messageInfo);
			if (!ObjectUtils.isEmpty(result)) {
				SendMessage message = new SendMessage();
				message.setChatId(messageInfo.getGroupId().toString());
				message.setText(result);
				message.setReplyToMessageId(messageInfo.getMessageId()); // 回复触发消息
				return message;
			}
		}
		return null;
	}

	@Override
	public Boolean isAdmin() {
		return null;
	}

	@Override
	public Boolean delete() {
		return true;
	}

}
