package com.mi.manchi.telegram.handler;

import com.mi.manchi.telegram.model.entity.MessageDTO;
import com.mi.manchi.telegram.model.entity.MessageInfo;
import com.mi.manchi.telegram.service.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CommandDispatcher {

	// 存储「指令-处理器」映射（key：指令字符串，value：对应的处理器）
	private final Map<String, MessageHandler> commandMap = new HashMap<>();

	private final List<MessageHandler> messageHandlers = new ArrayList<>();

	private final TelegramBotService telegramBotService;

	// 注入所有实现了 CommandHandler 接口的处理器（Spring 自动扫描）
	public CommandDispatcher(List<MessageHandler> handlers, TelegramBotService telegramBotService) {
		this.telegramBotService = telegramBotService;
		// 初始化映射：将每个处理器支持的指令与处理器绑定
		for (MessageHandler handler : handlers) {
			if (!ObjectUtils.isEmpty(handler.getSupportedCommands())) {
				for (String command : handler.getSupportedCommands()) {
					log.info("Adding command: {}", command);
					commandMap.put(command.trim().toLowerCase(), handler); // 忽略大小写
				}
			}
			else {
				messageHandlers.add(handler);
			}

		}
	}

	/**
	 * 分发指令到对应的处理器
	 * @return 回复消息（若指令不支持则返回 null）
	 */
	public void dispatch(MessageDTO data) {
		MessageInfo messageInfo = data.getMessageInfo();
		String text = messageInfo.getContent().trim().toLowerCase(); // 统一转为小写匹配
		if (text.startsWith("/")) {
			// 提取指令（忽略参数，如 "/积分 100" 仅取 "/积分"）
			String command = text.split(" ")[0];
			MessageHandler handler = commandMap.get(command);
			if (handler != null) {
				if (handler.isAdmin()
						&& !telegramBotService.isAdmin(messageInfo.getGroupId(), messageInfo.getMemberId())) {
					return;
				}
				SendMessage sendMessage = handler.handle(data);
				if (!ObjectUtils.isEmpty(sendMessage)) {
					Message message = telegramBotService.sendMessage(sendMessage);
					if (handler.delete()) {
						telegramBotService.scheduleMsgDelete(messageInfo.getGroupId(), message.getMessageId());
					}
				}
			}
		}
		else {
			messageHandlers.forEach(handler -> {
				SendMessage sendMessage = handler.handle(data);
				if (!ObjectUtils.isEmpty(sendMessage)) {
					Message message = telegramBotService.sendMessage(sendMessage);
					if (handler.delete()) {
						telegramBotService.scheduleMsgDelete(messageInfo.getGroupId(), message.getMessageId());
					}
				}
			});
		}
	}

}