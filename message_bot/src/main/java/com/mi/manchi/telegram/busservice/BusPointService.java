package com.mi.manchi.telegram.busservice;

import com.mi.manchi.telegram.entity.MessageInfo;

/**
 * 积分服务
 */
public interface BusPointService {

	/**
	 * 查询当前用户在这个群的积分
	 * @param memberId
	 * @param groupId
	 * @return
	 */
	Integer selectPointByChatId(Long memberId, Long groupId);

	// 增加积分
	void addPointByMessageInfo(MessageInfo info);

	void checkInAddPoint();

}
