package com.mi.manchi.telegram.entity.point;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("member_point_log")
public class MemberPointLog extends Model<MemberPointLog> {

	private static final long serialVersionUID = 1L;

	/**
	 * 群ID
	 */
	private Long groupId;

	/**
	 * 用户Id
	 */
	private Long memberId;

	/**
	 * 积分
	 */
	private Integer point;

	/**
	 * 动作
	 */
	private Integer action;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

}
