package com.mi.manchi.telegram.entity.point;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user_daily_point")
public class UserDailyPoint extends Model<UserDailyPoint> {

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 群ID
	 */
	private Long groupId;

	/**
	 * 用户Id
	 */
	private Long userId;

	/**
	 * 统计日期
	 */
	private LocalDate recordDate;

	/**
	 * 当日总积分
	 */
	private Integer totalPoint;

	/**
	 * 当日发言积分
	 */
	private Integer speakPoint;

	/**
	 * 当日签到积分
	 */
	private Integer checkinPoint;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

}
