package com.mi.manchi.telegram.busservice.impl;

import com.mi.manchi.telegram.busservice.BusBotAdminService;
import com.mi.manchi.telegram.config.GroupMessageBot;
import com.mi.manchi.telegram.handler.CommandDispatcher;
import com.mi.manchi.telegram.model.entity.GroupManageInfo;
import com.mi.manchi.telegram.model.entity.GroupMemberInfo;
import com.mi.manchi.telegram.model.entity.MessageDTO;
import com.mi.manchi.telegram.model.entity.MessageInfo;
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
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusBotAdminServiceImpl implements BusBotAdminService {

	private final GroupManageInfoService groupManageInfoService;

	private final GroupMemberInfoService groupMemberInfoService;

	private final MessageInfoService messageInfoService;

	private final CommandDispatcher commandDispatcher;

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
		log.info("parse user invited group:{}", data);
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
		log.info("parse invite link:{}", data);
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
		handDelBotMessage(text, groupId, messageId);
		MessageDTO data = new MessageDTO();
		data.setMessageInfo(messageInfo);
		commandDispatcher.dispatch(data);

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
