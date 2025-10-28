package com.mi.manchi.telegram.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 敏感词配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("config_sensitive_word")
public class SensitiveWord extends Model<SensitiveWord> {

	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 群组ID
	 */
	private Long groupId;

	/**
	 * 敏感词
	 */
	private String word;

	/**
	 * 敏感词处理方式 {@link com.mi.manchi.telegram.enums.SensitiveWordEnums.ProcessType}
	 */
	private Integer processType;

	/**
	 * 警告内容
	 */
	private String warnMessage;

	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;

}
