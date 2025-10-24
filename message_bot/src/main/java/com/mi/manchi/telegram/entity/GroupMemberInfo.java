package com.mi.manchi.telegram.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bot_group_member_info")
public class GroupMemberInfo extends Model<GroupMemberInfo> {

	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 机器人ID
	 */
	private Long botId;

	/**
	 * 群组ID
	 */
	private Long groupId;

	/**
	 * 群成员ID
	 */
	private Long memberId;

	/**
	 * 群成员名称
	 */
	private String groupName;

	/**
	 * 群成员 firstName
	 */
	private String memberFirstName;

	/**
	 * 群成员 lastName
	 */
	private String memberLastName;

	/**
	 * 群成员 username
	 */
	private String memberUsername;

	/**
	 * 邀请者ID
	 */
	private Long inviterId;

	/**
	 * 群成员角色
	 */
	private String role;

	/**
	 * 邀请者 firstName
	 */
	private String inviterFirstName;

	/**
	 * 邀请者 lastName
	 */
	private String inviterLastName;

	/**
	 * 邀请者 username
	 */
	private String inviterUsername;

	/**
	 * 邀请时间
	 */
	private Long inviteTime;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
