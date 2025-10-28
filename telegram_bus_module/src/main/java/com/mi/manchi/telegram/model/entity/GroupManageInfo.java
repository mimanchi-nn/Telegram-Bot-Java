package com.mi.manchi.telegram.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bot_group_manage_info")
public class GroupManageInfo extends Model<GroupManageInfo> {

	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 群组ID
	 */
	private Long groupId;

	/**
	 * 机器人ID
	 */
	private Long botId;

	/**
	 * 群组名称
	 */
	private String groupName;

	/**
	 * 群组类型
	 */
	private String groupType;

	/**
	 * 邀请人ID
	 */
	private Long inviterId;

	/**
	 * 角色
	 */
	private String role;

	/**
	 * 邀请人 firstName
	 */
	private String inviterFirstName;

	/**
	 * 邀请人 lastName
	 */
	private String inviterLastName;

	/**
	 * 邀请人 username
	 */
	private String inviterUsername;

	/**
	 * 邀请时间
	 */
	private Long inviteTime;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
