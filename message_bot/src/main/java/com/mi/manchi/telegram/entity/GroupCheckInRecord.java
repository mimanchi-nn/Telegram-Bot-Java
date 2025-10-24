package com.mi.manchi.telegram.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("bot_group_check_in_record")
public class GroupCheckInRecord extends Model<GroupCheckInRecord> {

	@TableId(type = IdType.AUTO) // 主键自增
	private Long id;

	// 群ID（区分不同群的签到数据）
	private Long groupId;

	// 用户ID（唯一标识用户）
	private Long userId;

	// 用户名（展示用）
	private String userName;

	// 今日签到时间（首次签到时间）
	private LocalDateTime todayCheckInTime;

	// 累计签到天数
	private Integer totalCheckInDays;

	// 连续签到天数
	private Integer continuousCheckInDays;

	// 最后一次签到日期（用于判断连续签到）
	private LocalDate lastCheckInDate;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
