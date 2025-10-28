package com.mi.manchi.telegram.handler;

import com.mi.manchi.telegram.model.entity.MessageDTO;
import com.mi.manchi.telegram.model.entity.MessageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class HandTriggerKeyWordHandler implements MessageHandler {

	private final List<String> triggerKeywords = Arrays.asList("舔狗", "爱你", "宝", "想你", "懒懒的狗");

	List<String> loveQuotes = Arrays.asList("你昨晚在梦里叫错了我的名，没事，我今天就去改。", "那年你吐在操场上的口香糖，我捡起来嚼了三年。",
			"今天晚上有点冷，刚刚偷电瓶的时候被发现了，本来想跑，结果警察说了一句老实点别动，我立刻就放弃了抵抗，因为我记得你说过你喜欢老实人。",
			"你说你情头是一个人用的，朋友圈空白是因为你不发，情侣空间是和闺蜜开的，每次聊天你都说在忙，你真是一个上进的好女孩，我好喜欢你。", "你们都说她只是在吊着我，那她怎么不去吊别人？嗯我懂，她一定是喜欢我。",
			"我偷偷的潜入了您的家里，想要拿走您的东西，我悄悄的走进卧室，您躺在床上睡觉，睫毛好长，窗帘没合好，调皮的月光洒到您身上，愣愣的看了半天，什么也没有拿，当小偷这么多年，我第一次被人偷了东西。",
			"今天我头疼去医院检查，结果那个医生说我脑子坏了。 我一听就把他打了一顿， 我的脑子里都是你，他居然说我脑子坏了。他说你坏！我不允许别人说你一点不好。",
			"今天在试卷上写满了你的名字，最后考了零分，果然爱你没有结果。", "我想戒掉熬夜和想你，好好做自己。", "你们都说她只是在吊着我，那她怎么不去吊别人？嗯我懂，她一定是喜欢我。", "汪汪汪!!!");

	@Override
	public String[] getSupportedCommands() {
		return new String[0];
	}

	@Override
	public SendMessage handle(MessageDTO data) {
		MessageInfo info = data.getMessageInfo();
		String text = info.getContent();
		String lowerText = text.toLowerCase();
		for (String key : triggerKeywords) {
			if (lowerText.contains(key)) {
				return sendLoveQuote(info.getGroupId(), info.getMessageId());
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
		return false;
	}

	/**
	 * 发送舔狗语录（对应 Go 的 sendLoveQuote 函数）
	 */
	private SendMessage sendLoveQuote(Long chatId, Integer replyToMessageId) {
		// 随机取一条
		String quote = loveQuotes.get((int) (Math.random() * loveQuotes.size()));
		SendMessage message = new SendMessage();
		message.setChatId(chatId.toString());
		message.setText(quote);
		message.setReplyToMessageId(replyToMessageId); // 回复触发消息
		return message;
	}

}
