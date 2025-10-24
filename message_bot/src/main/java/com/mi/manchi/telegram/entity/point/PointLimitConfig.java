package com.mi.manchi.telegram.entity.point;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("group_point_limit")
public class PointLimitConfig extends Model<PointLimitConfig> {

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 群ID
	 */
	private Long groupId;

	/**
	 * 每日上限积分
	 */
	private Integer dailyMaxPoint;

	/**
	 * 有效发言（超4字）得分数
	 */
	private Integer speakPointPerValid;

	/**
	 * 签到分数
	 */
	private Integer checkinPoint = 5;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;

}