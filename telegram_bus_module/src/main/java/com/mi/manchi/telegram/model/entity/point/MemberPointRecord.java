package com.mi.manchi.telegram.model.entity.point;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("member_point_record")
public class MemberPointRecord extends Model<MemberPointRecord> {

	@TableId(type = IdType.AUTO)
	private Long id;

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
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;

}
