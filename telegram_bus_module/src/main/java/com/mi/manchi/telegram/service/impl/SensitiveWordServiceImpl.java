package com.mi.manchi.telegram.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mi.manchi.telegram.model.entity.SensitiveWord;
import com.mi.manchi.telegram.mapper.SensitiveWordMapper;
import com.mi.manchi.telegram.service.SensitiveWordService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord>
		implements SensitiveWordService {

	@Override
	public IPage<SensitiveWord> selectPage(IPage<SensitiveWord> page, SensitiveWord data) {
		LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(!ObjectUtils.isEmpty(data.getGroupId()), SensitiveWord::getGroupId, data.getGroupId());
		return page(page, wrapper);
	}

}
