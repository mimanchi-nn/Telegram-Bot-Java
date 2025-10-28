package com.mi.manchi.telegram.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mi.manchi.telegram.model.entity.point.PointLimitConfig;
import com.mi.manchi.telegram.mapper.PointLimitConfigMapper;
import com.mi.manchi.telegram.service.PointLimitConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointLimitConfigServiceImpl extends ServiceImpl<PointLimitConfigMapper, PointLimitConfig>
		implements PointLimitConfigService {

	@Override
	public PointLimitConfig selectConfig(Long groupId) {
		LambdaQueryWrapper<PointLimitConfig> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(PointLimitConfig::getGroupId, groupId);
		return baseMapper.selectOne(wrapper);
	}

}
