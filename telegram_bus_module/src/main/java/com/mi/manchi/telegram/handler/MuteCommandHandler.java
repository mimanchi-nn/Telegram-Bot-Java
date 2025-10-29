package com.mi.manchi.telegram.handler;

import com.mi.manchi.telegram.config.GroupMessageBot;
import com.mi.manchi.telegram.model.entity.MessageDTO;
import com.mi.manchi.telegram.model.entity.MessageInfo;
import com.mi.manchi.telegram.utils.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.telegram.telegrambots.meta.api.methods.groupadministration.RestrictChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class MuteCommandHandler implements MessageHandler {

	private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d+)([smhd])");

	private static final List<String> SUPPORTED_UNITS = Arrays.asList("s", "m", "h", "d");

	private static final long MAX_MUTE_DURATION = 366 * 86400L; // 最大禁言时长：366天（Telegram限制）

	// 禁言权限配置：禁言后仅保留“查看消息”权限，禁止发送任何内容
	private static final ChatPermissions MUTE_PERMISSIONS = ChatPermissions.builder().canSendMessages(false) // 禁止发送文本消息
			.canSendMediaMessages(false) // 禁止发送媒体（图片/视频等）
			.canSendPolls(false) // 禁止发送投票
			.canSendOtherMessages(false) // 禁止发送其他消息（如贴纸/文件）
			.canAddWebPagePreviews(false) // 禁止预览链接
			.canChangeInfo(false) // 禁止修改群信息
			.canInviteUsers(false) // 禁止邀请用户
			.canPinMessages(false) // 禁止置顶消息
			.build();

	// 解除禁言权限配置：恢复默认发言权限（可根据群需求调整）
	private static final ChatPermissions UNMUTE_PERMISSIONS = ChatPermissions.builder().canSendMessages(true)
			.canSendMediaMessages(true).canSendPolls(true).canSendOtherMessages(true).canAddWebPagePreviews(true)
			.canInviteUsers(true).build();

	@Override
	public String[] getSupportedCommands() {
		return new String[] { "/mute", "/mute [时长]", "/unmute" };
	}

	@Override
	public SendMessage handle(MessageDTO data) {
		MessageInfo info = data.getMessageInfo();
		MessageInfo reply = data.getReplyDetail();
		if (ObjectUtils.isEmpty(reply)) {
			return null;
		}
		String content = info.getContent();
		if (content.startsWith("/mute")) {
			// 解析禁言时长（默认永久禁言）
			Long untilDate = parseMuteDuration(content);
			if (untilDate == null) {
				return createReply(info.getGroupId(), "❌ 时长格式错误！支持：10s(秒)、5m(分)、2h(时)、1d(天)");
			}
			// 执行禁言
			handleMute(info.getGroupId(), reply.getMemberId(), untilDate);
			String tip = untilDate == Long.MAX_VALUE ? "永久禁言" : String.format("禁言 %s（%d秒后自动解除）",
					extractDurationStr(content), untilDate - (System.currentTimeMillis() / 1000));
			return createReply(info.getGroupId(), String.format("✅ 已对用户 #%d 执行%s", reply.getMemberId(), tip));

		}
		else if (content.equals("/unmute")) {
			// 执行解除禁言
			handleUnmute(info.getGroupId(), reply.getMemberId());
			return createReply(info.getGroupId(), String.format("✅ 已解除用户 #%d 的禁言", reply.getMemberId()));
		}

		switch (content) {
		case "/mute 10s":
			handleMute(info.getGroupId(), reply.getMemberId(), System.currentTimeMillis() / 1000 + 10);
			break;
		case "/mute 30s":
			handleMute(info.getGroupId(), reply.getMemberId(), System.currentTimeMillis() / 1000 + 30);
			break;
		case "/unmute":
			handleUnmute(info.getGroupId(), reply.getMemberId());
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public Boolean isAdmin() {
		return true;
	}

	@Override
	public Boolean delete() {
		return false;
	}

	private void handleMute(Long chatId, Long userId, Long untilDate) {
		// 调用禁言API
		RestrictChatMember restrictRequest = RestrictChatMember.builder().chatId(chatId.toString()).userId(userId)
				.permissions(MUTE_PERMISSIONS).untilDate(untilDate != null ? untilDate.intValue() : null) // Unix时间戳（秒）
				.build();
		try {
			getAbsSender().execute(restrictRequest);
		}
		catch (TelegramApiException e) {
			log.error("mute user {} failed,e:{}", userId, e.getMessage(), e);
		}
	}

	private void handleUnmute(Long chatId, Long userId) {
		// 调用解除禁言API（untilDate填0表示立即解除）
		RestrictChatMember restrictRequest = RestrictChatMember.builder().chatId(chatId.toString()).userId(userId)
				.permissions(UNMUTE_PERMISSIONS).untilDate(0) // 0 表示解除禁言（忽略之前的禁言时长）
				.build();
		try {
			getAbsSender().execute(restrictRequest);
		}
		catch (TelegramApiException e) {
			log.error("unmute user {} failed,e:{}", userId, e.getMessage(), e);
		}
	}

	/**
	 * 获取AbsSender实例（需要根据实际项目注入方式实现） 例如：从Spring容器中获取TelegramBot实例（该实例继承AbsSender）
	 */
	private AbsSender getAbsSender() {
		// 实际项目中需替换为真实的AbsSender获取逻辑
		return SpringUtil.getBean(GroupMessageBot.class);

	}

	/**
	 * 解析禁言时长（支持 s/m/h/d 单位）
	 * @return 禁言结束时间戳（秒），Long.MAX_VALUE 表示永久禁言，null 表示格式错误
	 */
	private Long parseMuteDuration(String content) {
		Matcher matcher = DURATION_PATTERN.matcher(content);
		if (!matcher.find()) {
			// 无时长参数 → 永久禁言
			return Long.MAX_VALUE;
		}

		try {
			int value = Integer.parseInt(matcher.group(1));
			String unit = matcher.group(2);

			// 校验单位合法性
			if (!SUPPORTED_UNITS.contains(unit)) {
				return null;
			}

			// 计算秒数
			long seconds;
			switch (unit) {
			case "s":
				seconds = value;
				break;
			case "m":
				seconds = value * 60;
				break;
			case "h":
				seconds = value * 3600;
				break;
			case "d":
				seconds = value * 86400;
				break;
			default:
				return null;
			}

			// 校验最大时长
			if (seconds > MAX_MUTE_DURATION) {
				return null;
			}

			// 计算结束时间戳（当前时间 + 秒数）
			return (System.currentTimeMillis() / 1000) + seconds;
		}
		catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * 创建回复消息
	 */
	private SendMessage createReply(Long chatId, String text) {
		return SendMessage.builder().chatId(chatId.toString()).text(text).build();
	}

	/**
	 * 提取时长字符串（用于回复提示）
	 */
	private String extractDurationStr(String content) {
		Matcher matcher = DURATION_PATTERN.matcher(content);
		return matcher.find() ? matcher.group() : "永久";
	}

}
