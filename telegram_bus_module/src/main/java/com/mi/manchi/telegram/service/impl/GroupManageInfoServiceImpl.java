package com.mi.manchi.telegram.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mi.manchi.telegram.model.entity.GroupManageInfo;
import com.mi.manchi.telegram.mapper.GroupManageInfoMapper;
import com.mi.manchi.telegram.service.GroupManageInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupManageInfoServiceImpl extends ServiceImpl<GroupManageInfoMapper, GroupManageInfo>
		implements GroupManageInfoService {

	@Override
	public GroupManageInfo selectGroup(Long groupId, Long botId) {
		LambdaQueryWrapper<GroupManageInfo> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(GroupManageInfo::getGroupId, groupId);
		wrapper.eq(GroupManageInfo::getBotId, botId);
		return baseMapper.selectOne(wrapper);
	}

}
