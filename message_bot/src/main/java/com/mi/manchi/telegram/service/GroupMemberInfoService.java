package com.mi.manchi.telegram.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mi.manchi.telegram.entity.GroupMemberInfo;

public interface GroupMemberInfoService extends IService<GroupMemberInfo> {

	/**
	 * 查询当前群组中群成员信息
	 * @param groupId
	 * @param memberId
	 * @return
	 */
	GroupMemberInfo selectMemberInfo(Long groupId, Long memberId);

}
