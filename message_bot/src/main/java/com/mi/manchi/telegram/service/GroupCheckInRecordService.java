package com.mi.manchi.telegram.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mi.manchi.telegram.entity.GroupCheckInRecord;

public interface GroupCheckInRecordService extends IService<GroupCheckInRecord> {

	/**
	 * 查询用户在群聊中的签到记录
	 * @param groupId 群聊ID
	 * @param userId 用户ID
	 * @return 签到记录
	 */
	GroupCheckInRecord selectCheckInRecord(Long groupId, Long userId);

}
