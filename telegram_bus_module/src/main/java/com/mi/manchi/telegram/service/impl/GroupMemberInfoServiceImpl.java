package com.mi.manchi.telegram.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mi.manchi.telegram.mapper.GroupMemberInfoMapper;
import com.mi.manchi.telegram.model.entity.GroupMemberInfo;
import com.mi.manchi.telegram.service.GroupMemberInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupMemberInfoServiceImpl extends ServiceImpl<GroupMemberInfoMapper, GroupMemberInfo>
		implements GroupMemberInfoService {

	@Override
	public GroupMemberInfo selectMemberInfo(Long groupId, Long memberId) {
		return baseMapper.selectOne(new LambdaQueryWrapper<GroupMemberInfo>().eq(GroupMemberInfo::getGroupId, groupId)
				.eq(GroupMemberInfo::getMemberId, memberId));
	}

}
