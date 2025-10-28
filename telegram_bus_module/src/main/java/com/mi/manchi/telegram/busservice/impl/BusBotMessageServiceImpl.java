package com.mi.manchi.telegram.busservice.impl;

import com.mi.manchi.telegram.busservice.BusBotMessageService;
import com.mi.manchi.telegram.model.entity.GroupCheckInRecord;
import com.mi.manchi.telegram.model.entity.MessageInfo;
import com.mi.manchi.telegram.service.GroupCheckInRecordService;
import com.mi.manchi.telegram.utils.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class BusBotMessageServiceImpl implements BusBotMessageService {

	private final GroupCheckInRecordService groupCheckInRecordService;

	@Override
	public String doCheckIn(MessageInfo info) {
		GroupCheckInRecord record = groupCheckInRecordService.selectCheckInRecord(info.getGroupId(),
				info.getMemberId());
		if (ObjectUtils.isEmpty(record)) {
			GroupCheckInRecord checkInRecord = new GroupCheckInRecord();
			checkInRecord.setGroupId(info.getGroupId());
			checkInRecord.setUserId(info.getMemberId());
			checkInRecord.setUserName(info.getSendUsername());
			checkInRecord.setTodayCheckInTime(DateTimeUtils.get());
			checkInRecord.setTotalCheckInDays(1);
			checkInRecord.setContinuousCheckInDays(1);
			checkInRecord.setLastCheckInDate(LocalDate.now());
			groupCheckInRecordService.save(checkInRecord);
			return buildCheckInSuccessMsg(checkInRecord.getTodayCheckInTime(), 1, 1);
		}
		LocalDate today = DateTimeUtils.get().toLocalDate();
		LocalDate yesterday = DateTimeUtils.get().toLocalDate().minusDays(1);
		LocalDateTime todayCheckInTime = ObjectUtils.isEmpty(record.getTodayCheckInTime()) ? DateTimeUtils.get()
				: record.getTodayCheckInTime();
		if (todayCheckInTime != null && todayCheckInTime.toLocalDate().equals(today)) {
			// 今日已签：返回当前状态
			return buildCheckInRepeatMsg(todayCheckInTime, record.getTotalCheckInDays(),
					record.getContinuousCheckInDays());
		}
		if (!ObjectUtils.isEmpty(todayCheckInTime) && todayCheckInTime.toLocalDate().equals(yesterday)) {
			// 昨日已签：连续签到+1
			record.setTodayCheckInTime(DateTimeUtils.get());
			record.setContinuousCheckInDays(record.getContinuousCheckInDays() + 1);
			record.setTotalCheckInDays(record.getTotalCheckInDays() + 1);
			record.setLastCheckInDate(today);
			record.setUpdateTime(DateTimeUtils.get());
			// 更新数据库
			groupCheckInRecordService.updateById(record);
			return buildCheckInSuccessMsg(record.getTodayCheckInTime(), record.getTotalCheckInDays(),
					record.getContinuousCheckInDays());
		}
		if (!ObjectUtils.isEmpty(todayCheckInTime) && !todayCheckInTime.toLocalDate().equals(yesterday)) {
			// 昨日未签：连续签到重置为1
			record.setTodayCheckInTime(DateTimeUtils.get());
			record.setContinuousCheckInDays(1);
			record.setTotalCheckInDays(record.getTotalCheckInDays() + 1);
			record.setLastCheckInDate(today);
			record.setUpdateTime(DateTimeUtils.get());
			// 更新数据库
			groupCheckInRecordService.updateById(record);
			return buildCheckInSuccessMsg(record.getTodayCheckInTime(), record.getTotalCheckInDays(),
					record.getContinuousCheckInDays());
		}
		return null;
	}

	/**
	 * 构建重复签到消息
	 */
	private String buildCheckInRepeatMsg(LocalDateTime checkInTime, int totalDays, int continuousDays) {
		return String.format("⚠️ 你今日已签到啦！\n" + "📅 今日签到时间：%s\n" + "🔢 累计签到：%d天\n" + "🔄 连续签到：%d天\n" + "💡 明天再来哦～",
				formatDateTime(checkInTime), totalDays, continuousDays);
	}

	/**
	 * 构建签到成功消息
	 */
	private String buildCheckInSuccessMsg(LocalDateTime checkInTime, int totalDays, int continuousDays) {
		return String.format("✅ 签到成功！\n" + "📅 签到时间：%s\n" + "🔢 累计签到：%d天\n" + "🔄 连续签到：%d天\n",
				// "💡 发送 /checkin_rank 查看群排名，/checkin_me 查看个人统计～",
				formatDateTime(checkInTime), totalDays, continuousDays);
	}

	private String formatDateTime(LocalDateTime dateTime) {
		if (dateTime == null) {
			return "未签到";
		}
		return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

}
