package com.mi.manchi.telegram.busservice.impl;

import com.mi.manchi.telegram.busservice.BusBotAdminService;
import com.mi.manchi.telegram.busservice.BusBotMessageService;
import com.mi.manchi.telegram.config.GroupMessageBot;
import com.mi.manchi.telegram.entity.GroupManageInfo;
import com.mi.manchi.telegram.entity.GroupMemberInfo;
import com.mi.manchi.telegram.entity.MessageInfo;
import com.mi.manchi.telegram.service.GroupManageInfoService;
import com.mi.manchi.telegram.service.GroupMemberInfoService;
import com.mi.manchi.telegram.service.MessageInfoService;
import com.mi.manchi.telegram.utils.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusBotAdminServiceImpl implements BusBotAdminService {

	private final GroupManageInfoService groupManageInfoService;

	private final GroupMemberInfoService groupMemberInfoService;

	private final MessageInfoService messageInfoService;

	private final BusBotMessageService busBotMessageService;

	private final List<String> spamKeywords = Arrays.asList("广告", "兼职", "刷单", "二维码","全网最低");

	@Override
	public void processBotJoinGroup(ChatMemberUpdated data) {
		// 新成员信息 被邀请者（机器人）状态判断：是否成功加入
		ChatMember member = data.getNewChatMember();
		// 群信息
		Chat groupChat = data.getChat();
		// 邀请者信息
		User inviter = data.getFrom();
		Long groupId = groupChat.getId(); // 群 ID（关键，后续群管理用）
		String groupName = groupChat.getTitle(); // 群名称
		String groupType = groupChat.getType(); // 群类型（group/supergroup）
		Long botId = member.getUser().getId();
		// 邀请者信息
		Long inviterId = inviter.getId(); // 邀请者用户 ID

		if (member.getStatus().equals("member") || member.getStatus().equals("administrator")) {
			GroupManageInfo manageInfo = groupManageInfoService.selectGroup(groupId, botId);
			if (ObjectUtils.isEmpty(manageInfo)) {
				GroupManageInfo info = new GroupManageInfo();
				info.setGroupId(groupId);
				info.setGroupName(groupName);
				info.setGroupType(groupType);
				info.setBotId(member.getUser().getId());
				info.setRole(member.getStatus());
				info.setInviterId(inviterId);
				info.setInviterFirstName(inviter.getFirstName());
				info.setInviterLastName(inviter.getLastName());
				info.setInviterUsername(inviter.getUserName());
				info.setInviteTime(data.getDate() * 1000L);
				info.setCreateTime(LocalDateTime.now());
				info.setUpdateTime(LocalDateTime.now());
				groupManageInfoService.save(info);
			}
			else {
				manageInfo.setRole(member.getStatus());
				manageInfo.setInviteTime(data.getDate() * 1000L);
				manageInfo.setUpdateTime(LocalDateTime.now());
				groupManageInfoService.updateById(manageInfo);
			}
		}
	}

	@Override
	public void processUserInvitedToGroup(ChatMemberUpdated data) {
		// 群信息（同上面逻辑）
		Chat groupChat = data.getChat();
		long groupId = groupChat.getId();
		String groupName = groupChat.getTitle();
		// 邀请者信息
		User inviter = data.getFrom();
		// 被邀请的新用户信息
		ChatMember newUser = data.getNewChatMember();
		User invitedUser = newUser.getUser();
		long invitedUserId = invitedUser.getId();

		// 判断是否为“新用户被邀请入群”
		if (newUser.getStatus().equals("member")) {
			GroupMemberInfo memberInfo = groupMemberInfoService.selectMemberInfo(groupId, invitedUserId);
			if (ObjectUtils.isEmpty(memberInfo)) {
				GroupMemberInfo info = new GroupMemberInfo();
				info.setGroupId(groupId);
				info.setMemberId(invitedUserId);
				info.setGroupName(groupName);
				info.setInviterId(inviter.getId());
				info.setRole(newUser.getStatus());
				info.setInviterFirstName(inviter.getFirstName());
				info.setInviterLastName(inviter.getLastName());
				info.setInviterUsername(inviter.getUserName());
				info.setMemberFirstName(invitedUser.getFirstName());
				info.setMemberLastName(invitedUser.getLastName());
				info.setMemberUsername(invitedUser.getUserName());
				info.setInviteTime(data.getDate() * 1000L);
				info.setCreateTime(LocalDateTime.now());
				info.setUpdateTime(LocalDateTime.now());
				log.info("新用户入群:{}", info);
				groupMemberInfoService.save(info);
				Integer messageId = sendWelcomeMessage(groupId, invitedUser.getFirstName(), invitedUserId);
				if (ObjectUtils.isEmpty(messageId)) {
					log.error("删除欢迎消息失败：{}", messageId);
				}
				scheduleWelcomeMsgDelete(groupId, messageId);
			}

		}
	}

	@Override
	public void parseInviteLinkCallback(CallbackQuery data) {
		// 解析回调数据中的邀请链接信息
		String callbackData = data.getData();
		// 提取邀请链接中的参数（如：inviterId=123456&groupId=789012）
		// 这里简单假设回调数据格式为：inviterId=123456&groupId=789012
		String[] params = callbackData.split("&");
		for (String param : params) {
			String[] keyValue = param.split("=");
			if (keyValue.length == 2) {
				String key = keyValue[0];
				String value = keyValue[1];
				// 根据key进行处理，如：根据inviterId查询邀请者信息
				if (key.equals("inviterId")) {
					Long inviterId = Long.parseLong(value);
					// 调用服务层方法查询邀请者信息
					// User inviter = userService.findUserById(inviterId);
					log.info("根据inviterId查询到邀请者信息：{}", inviterId);
				}
			}
		}
	}

	@Override
	public void processGroupMessage(Message message) {
		// 1. 提取消息基本信息
		Long groupId = message.getChatId(); // 群 ID
		Integer messageId = message.getMessageId(); // 消息 ID
		User sender = message.getFrom(); // 发送者
		String text = message.getText(); // 消息内容
		int sendTime = message.getDate(); // 发送时间戳（秒）
		Long senderId = sender.getId();
		String senderName = sender.getFirstName() + (sender.getLastName() != null ? " " + sender.getLastName() : "");
		String senderUsername = sender.getUserName();
		log.info("群聊 [{}] 收到消息：ID={}, 发送者={}（ID={}）, 内容：{}", groupId, messageId, senderName, senderId, text);
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setGroupId(groupId);
		messageInfo.setMessageId(messageId);
		messageInfo.setMemberId(senderId);
		messageInfo.setSendTime(sendTime);
		messageInfo.setContent(text);
		messageInfo.setSendFirstName(sender.getFirstName());
		messageInfo.setSendLastName(sender.getLastName());
		messageInfo.setSendUsername(senderUsername);
		messageInfo.setCreateTime(LocalDateTime.now());
		messageInfo.setUpdateTime(LocalDateTime.now());
		messageInfoService.save(messageInfo);
		handSpamMessage(text, groupId, messageId);
		handTriggerKeyWords(text, groupId, messageId);
		handDelBotMessage(text, groupId, messageId);
		handCheckIn(messageInfo);
	}

	private void handCheckIn(MessageInfo messageInfo) {
		if (messageInfo.getContent().contains("爱懒懒")) {
			String string = busBotMessageService.doCheckIn(messageInfo);
			if (!ObjectUtils.isEmpty(string)) {
				SendMessage message = new SendMessage();
				message.setChatId(messageInfo.getGroupId().toString());
				message.setText(string);
				message.setReplyToMessageId(messageInfo.getMessageId()); // 回复触发消息

				try {
					Integer messageId = sendMessage(message);
					scheduleWelcomeMsgDelete(messageInfo.getGroupId(), messageId);
				}
				catch (Exception e) {
					log.error("send love quote failed：{}", e.getMessage(), e);
				}
			}
		}
	}

	private void handDelBotMessage(String text, Long chatId, Integer messageId) {
		if (text.contains("_bot") && text.contains("@")) {
			DeleteMessage deleteMsg = new DeleteMessage();
			deleteMsg.setChatId(chatId.toString());
			deleteMsg.setMessageId(messageId);
			try {
				deleteMessage(deleteMsg);
			}
			catch (TelegramApiException e) {
				log.error("删除包含机器人名称的消息失败：", e);
			}
		}
	}

	private void handTriggerKeyWords(String text, Long chatId, Integer messageId) {
		List<String> triggerKeywords = Arrays.asList("舔狗", "爱你", "宝", "想你", "懒懒的狗");
		String lowerText = text.toLowerCase(); // 转为小写，实现忽略大小写匹配
		for (String keyword : triggerKeywords) {
			if (lowerText.contains(keyword)) {
				sendLoveQuote(chatId, messageId); // 发送舔狗语录
				return; // 只触发一次，避免重复发送
			}
		}

	}

	private void handSpamMessage(String text, Long chatId, Integer messageId) {
		if (filterSpamMessage(text)) {
			DeleteMessage deleteMsg = new DeleteMessage();
			deleteMsg.setChatId(chatId.toString());
			deleteMsg.setMessageId(messageId);
			try {
				deleteMessage(deleteMsg);
			}
			catch (TelegramApiException e) {
				log.error("del spam msg failed：{}", e.getMessage(), e);
			}
		}

	}

	private Boolean filterSpamMessage(String text) {
		return spamKeywords.stream().anyMatch(text::contains);
	}

	/**
	 * 发送舔狗语录（对应 Go 的 sendLoveQuote 函数）
	 */
	private void sendLoveQuote(Long chatId, Integer replyToMessageId) {
		// 示例：随机选择一条舔狗语录（实际可从数据库/配置文件加载）
		List<String> loveQuotes = Arrays.asList("你昨晚在梦里叫错了我的名，没事，我今天就去改。", "那年你吐在操场上的口香糖，我捡起来嚼了三年。",
				"今天晚上有点冷，刚刚偷电瓶的时候被发现了，本来想跑，结果警察说了一句老实点别动，我立刻就放弃了抵抗，因为我记得你说过你喜欢老实人。",
				"你说你情头是一个人用的，朋友圈空白是因为你不发，情侣空间是和闺蜜开的，每次聊天你都说在忙，你真是一个上进的好女孩，我好喜欢你。", "你们都说她只是在吊着我，那她怎么不去吊别人？嗯我懂，她一定是喜欢我。",
				"我偷偷的潜入了您的家里，想要拿走您的东西，我悄悄的走进卧室，您躺在床上睡觉，睫毛好长，窗帘没合好，调皮的月光洒到您身上，愣愣的看了半天，什么也没有拿，当小偷这么多年，我第一次被人偷了东西。",
				"今天我头疼去医院检查，结果那个医生说我脑子坏了。 我一听就把他打了一顿， 我的脑子里都是你，他居然说我脑子坏了。他说你坏！我不允许别人说你一点不好。",
				"今天在试卷上写满了你的名字，最后考了零分，果然爱你没有结果。", "我想戒掉熬夜和想你，好好做自己。", "你们都说她只是在吊着我，那她怎么不去吊别人？嗯我懂，她一定是喜欢我。", "汪汪汪!!!");
		// 随机取一条
		String quote = loveQuotes.get((int) (Math.random() * loveQuotes.size()));
		SendMessage message = new SendMessage();
		message.setChatId(chatId.toString());
		message.setText(quote);
		message.setReplyToMessageId(replyToMessageId); // 回复触发消息

		try {
			sendMessage(message);
		}
		catch (Exception e) {
			log.error("send love quote failed：{}", e.getMessage(), e);
		}
	}

	private Integer sendWelcomeMessage(Long groupId, String memberName, Long memberId) {
		SendMessage welcomeMsg = getSendMessage(groupId, memberName, memberId);
		try {
			// 发送消息并获取返回结果（包含消息ID）
			Integer sentMsgId = sendMessage(welcomeMsg);
			log.info("已向群 [{}] 发送欢迎语，消息ID：{}", groupId, sentMsgId);
			return sentMsgId; // 返回消息ID，用于删除
		}
		catch (Exception e) {
			log.error("发送欢迎语失败（群ID：{}）：", groupId, e);
			return null;
		}
	}

	@NotNull
	private static SendMessage getSendMessage(Long groupId, String memberName, Long memberId) {
		String welcomeText = String.format("🎉  欢迎 <a href=\"tg://user?id=%d\">%s</a> 加入! \n" + "📌 进群请先阅读群公告，遵守群规～\n"
				+ "💡 有问题可发送 /help 查看机器人功能～", memberId, memberName);
		SendMessage welcomeMsg = new SendMessage();
		welcomeMsg.setChatId(groupId.toString()); // 群ID（转为字符串，避免Long类型异常）
		welcomeMsg.setText(welcomeText);
		// 可选：启用Markdown格式（需注意转义特殊字符）
		welcomeMsg.setParseMode("HTML");
		return welcomeMsg;
	}

	private void scheduleWelcomeMsgDelete(Long groupId, Integer welcomeMsgId) {
		Timer timer = new Timer(); // 定时任务调度器
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				DeleteMessage deleteMsg = new DeleteMessage();
				deleteMsg.setChatId(groupId.toString());
				deleteMsg.setMessageId(welcomeMsgId);

				try {
					deleteMessage(deleteMsg);
					log.info("已定时删除群 [{}] 的欢迎语，消息ID：{}", groupId, welcomeMsgId);
				}
				catch (TelegramApiException e) {
					log.error("定时删除欢迎语失败（群ID：{}，消息ID：{}）：", groupId, welcomeMsgId, e);
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
