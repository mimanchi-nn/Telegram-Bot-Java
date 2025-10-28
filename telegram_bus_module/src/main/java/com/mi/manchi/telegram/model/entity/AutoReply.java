package com.mi.manchi.telegram.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class AutoReply extends Model<AutoReply> {

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 群组ID
	 */
	private Long groupId;

	/**
	 * 关键词
	 */
	private String keyword;

	/**
	 * 回复内容
	 */
	private String reply;

	/**
	 * 匹配类型
	 */
	private Integer matchType;

	/**
	 * 状态 1启用 0禁用
	 */
	private Integer status;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

}
