package com.mi.manchi.telegram.busservice.impl;

import com.mi.manchi.telegram.busservice.BusPointService;
import com.mi.manchi.telegram.entity.MessageInfo;
import com.mi.manchi.telegram.entity.point.MemberPointRecord;
import com.mi.manchi.telegram.entity.point.PointLimitConfig;
import com.mi.manchi.telegram.service.PointLimitConfigService;
import com.mi.manchi.telegram.utils.JacksonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class BusPointServiceImpl implements BusPointService {

    private final PointLimitConfigService pointLimitConfigService;

    private final MemberPointRecordService memberPointRecordService;

    public final HashMap<Long, String> pointMap = new HashMap<>();


    @Override
    public Integer selectPointByChatId(Long memberId, Long groupId) {
        return 0;
    }

    @Override
    public void addPointByMessageInfo(MessageInfo info) {
        String value = pointMap.get(info.getGroupId());
        // 无配置
        if ("NULL".equals(value)) {
            return;
        }
        if (ObjectUtils.isEmpty(value)) {
            PointLimitConfig config = pointLimitConfigService.selectConfig(info.getGroupId());
            if (ObjectUtils.isEmpty(config)) {
                pointMap.put(info.getGroupId(), "NULL");
                return;
            }else {
                pointMap.put(info.getGroupId(), JacksonUtils.toJson(config));
            }
        }else {
           PointLimitConfig config = JacksonUtils.toObj(value, PointLimitConfig.class);
        }


        if (ObjectUtils.isEmpty(config)) {
            PointLimitConfig pointLimitConfig = pointLimitConfigService.selectConfig(info.getGroupId());
            if (pointLimitConfig != null) {
                config = pointLimitConfig;
                pointMap.put(info.getGroupId(), config);
            }else {
                return;
            }
        }

    }

    @Override
    public void checkInAddPoint() {

    }


    private void processMessagePoint(PointLimitConfig config,MessageInfo info) {
        Integer valid = config.getSpeakPointPerValid();
        // 无效发言
        if (info.getContent().length() < valid) {
            return;
        }
        // TODO 判断是否达到上限
       // 添加积分
        addPoint(info.getMemberId(), info.getGroupId(), config.getSpeakPointPerValid());
    }


    private void addPoint(Long memberId, String groupId, int valid) {

    }
}
