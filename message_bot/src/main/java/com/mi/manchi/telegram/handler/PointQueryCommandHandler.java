package com.mi.manchi.telegram.handler;

import com.mi.manchi.telegram.config.GroupMessageBot;
import com.mi.manchi.telegram.entity.MessageDTO;
import com.mi.manchi.telegram.entity.MessageInfo;
import com.mi.manchi.telegram.entity.point.MemberPointRecord;
import com.mi.manchi.telegram.service.MemberPointRecordService;
import com.mi.manchi.telegram.utils.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * 积分查询指令处理器：处理 /point、/积分 等指令
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PointQueryCommandHandler implements MessageHandler {

	private final MemberPointRecordService memberPointRecordService;

	// 支持的指令：/point、/积分、/我的积分
	@Override
	public String[] getSupportedCommands() {
		return new String[] { "/point", "/积分", "/我的积分" };
	}

	@Override
	public SendMessage handle(MessageDTO data) {
		MessageInfo info = data.getMessageInfo();
		// 调用积分服务查询用户积分
		MemberPointRecord record = memberPointRecordService.selectRecord(info.getGroupId(), info.getMemberId());
		String userName = info.getSendFirstName();
		Integer points = ObjectUtils.isEmpty(record) ? 0 : record.getPoint();
		// 构建回复消息
		String text = String.format("💳 【%s 的积分信息】\n" + "当前可用积分：%d\n", userName, points);

		return SendMessage.builder().chatId(info.getGroupId().toString()).text(text).build();
	}

	@Override
	public Boolean isAdmin() {
		return false;
	}

	@Override
	public Boolean delete() {
		return true;
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

}