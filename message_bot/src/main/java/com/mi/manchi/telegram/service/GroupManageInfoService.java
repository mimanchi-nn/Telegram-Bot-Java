package com.mi.manchi.telegram.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mi.manchi.telegram.entity.GroupManageInfo;

public interface GroupManageInfoService extends IService<GroupManageInfo> {

	GroupManageInfo selectGroup(Long groupId, Long botId);

}
