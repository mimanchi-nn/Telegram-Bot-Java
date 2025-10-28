package com.mi.manchi.telegram.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mi.manchi.telegram.model.entity.point.PointLimitConfig;

public interface PointLimitConfigService extends IService<PointLimitConfig> {

	/**
	 * 查询当前群组积分配置
	 * @param groupId
	 * @return
	 */
	PointLimitConfig selectConfig(Long groupId);

}
