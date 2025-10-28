package com.mi.manchi.telegram.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 快捷指令
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ShortCommand extends Model<ShortCommand> {

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

}
