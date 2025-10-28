package com.mi.manchi.telegram.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mi.manchi.telegram.model.entity.point.MemberPointRecord;
import com.mi.manchi.telegram.mapper.MemberPointRecordMapper;
import com.mi.manchi.telegram.service.MemberPointRecordService;
import org.springframework.stereotype.Service;

@Service
public class MemberPointRecordServiceImpl extends ServiceImpl<MemberPointRecordMapper, MemberPointRecord>
		implements MemberPointRecordService {

	@Override
	public MemberPointRecord selectRecord(Long groupId, Long memberId) {
		LambdaQueryWrapper<MemberPointRecord> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(MemberPointRecord::getGroupId, groupId).eq(MemberPointRecord::getMemberId, memberId);
		return baseMapper.selectOne(wrapper);
	}

}
