package com.mi.manchi.telegram.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mi.manchi.telegram.entity.GroupCheckInRecord;
import com.mi.manchi.telegram.mapper.GroupCheckInRecordMapper;
import com.mi.manchi.telegram.service.GroupCheckInRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupCheckInRecordServiceImpl extends ServiceImpl<GroupCheckInRecordMapper, GroupCheckInRecord>
		implements GroupCheckInRecordService {

	@Override
	public GroupCheckInRecord selectCheckInRecord(Long groupId, Long userId) {
		LambdaQueryWrapper<GroupCheckInRecord> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(GroupCheckInRecord::getGroupId, groupId);
		wrapper.eq(GroupCheckInRecord::getUserId, userId);
		wrapper.last("limit 1");
		return baseMapper.selectOne(wrapper);
	}

}
