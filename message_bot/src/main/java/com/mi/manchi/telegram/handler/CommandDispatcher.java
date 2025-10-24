package com.mi.manchi.telegram.handler;

import com.mi.manchi.telegram.config.GroupMessageBot;
import com.mi.manchi.telegram.entity.MessageDTO;
import com.mi.manchi.telegram.entity.MessageInfo;
import com.mi.manchi.telegram.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
@Slf4j
public class CommandDispatcher {

	// 存储「指令-处理器」映射（key：指令字符串，value：对应的处理器）
	private final Map<String, MessageHandler> commandMap = new HashMap<>();

	private final List<MessageHandler> messageHandlers = new ArrayList<>();

	// 注入所有实现了 CommandHandler 接口的处理器（Spring 自动扫描）
	public CommandDispatcher(List<MessageHandler> handlers) {
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
				SendMessage sendMessage = handler.handle(data);
				if (!ObjectUtils.isEmpty(sendMessage)) {
					Integer messageId = sendMessage(sendMessage);
					if (handler.delete()) {
						scheduleMsgDelete(messageInfo.getGroupId(), messageId);
					}
				}
			}
		}
		else {
			messageHandlers.forEach(handler -> {
				SendMessage sendMessage = handler.handle(data);
				if (!ObjectUtils.isEmpty(sendMessage)) {
					Integer messageId = sendMessage(sendMessage);
					if (handler.delete()) {
						scheduleMsgDelete(messageInfo.getGroupId(), messageId);
					}
				}
			});
		}
	}

	private void scheduleMsgDelete(Long groupId, Integer welcomeMsgId) {
		// 定时任务调度器
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				DeleteMessage deleteMsg = new DeleteMessage();
				deleteMsg.setChatId(groupId.toString());
				deleteMsg.setMessageId(welcomeMsgId);

				try {
					deleteMessage(deleteMsg);
					log.info("已定时删除群 [{}] 的消息，消息ID：{}", groupId, welcomeMsgId);
				}
				catch (TelegramApiException e) {
					log.error("定时删除消息失败（群ID：{}，消息ID：{}）：", groupId, welcomeMsgId, e);
				}
				finally {
					timer.cancel(); // 任务执行后关闭定时器，避免资源泄漏
				}
			}
		}, 10000); // 延迟DELETE_DELAY毫秒后执行
	}

	private Integer sendMessage(BotApiMethodMessage message) {
		GroupMessageBot bean = SpringUtil.getBean(GroupMessageBot.class);
		try {
			Message execute = bean.execute(message);
			return execute.getMessageId();
		}
		catch (Exception e) {
			log.error("send message failed：{}", e.getMessage(), e);
		}
		return null;
	}

	private void deleteMessage(DeleteMessage deleteMsg) throws TelegramApiException {
		GroupMessageBot bean = SpringUtil.getBean(GroupMessageBot.class);
		bean.execute(deleteMsg);

	}

}