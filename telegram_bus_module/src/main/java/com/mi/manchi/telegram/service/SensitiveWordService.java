package com.mi.manchi.telegram.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mi.manchi.telegram.model.entity.SensitiveWord;

public interface SensitiveWordService extends IService<SensitiveWord> {

	IPage<SensitiveWord> selectPage(IPage<SensitiveWord> page, SensitiveWord data);

}
