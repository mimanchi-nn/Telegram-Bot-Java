package com.mi.manchi.telegram.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mi.manchi.telegram.entity.point.MemberPointRecord;
import com.mi.manchi.telegram.mapper.MemberPointRecordMapper;
import com.mi.manchi.telegram.service.MemberPointRecordService;
import org.springframework.stereotype.Service;

@Service
public class MemberPointRecordServiceImpl extends ServiceImpl<MemberPointRecordMapper, MemberPointRecord>
        implements MemberPointRecordService {

}
