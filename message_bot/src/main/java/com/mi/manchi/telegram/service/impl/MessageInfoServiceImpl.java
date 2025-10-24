package com.mi.manchi.telegram.service.impl;

import com.mi.manchi.telegram.service.MessageInfoService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mi.manchi.telegram.entity.MessageInfo;
import com.mi.manchi.telegram.mapper.MessageInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessageInfo> implements MessageInfoService {

}
