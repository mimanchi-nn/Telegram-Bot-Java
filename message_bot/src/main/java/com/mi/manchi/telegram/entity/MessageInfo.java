package com.mi.manchi.telegram.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("bot_message_info")
public class MessageInfo extends Model<MessageInfo> {

	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 群组ID
	 */
	private Long groupId;

	/**
	 * 消息ID
	 */
	private Integer messageId;

	/**
	 * 发送者ID
	 */
	private Long memberId;

	/**
	 * 发送时间
	 */
	private Integer sendTime;

	/**
	 * 发送内容
	 */
	private String content;

	/**
	 * 发送者 firstName
	 */
	private String sendFirstName;

	/**
	 * 发送者 lastName
	 */
	private String sendLastName;

	/**
	 * 发送者 username
	 */
	private String sendUsername;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
