package com.mboot.service.impl;

import com.mboot.entity.Log;
import com.mboot.mapper.db1.LogMapper;
import com.mboot.service.LogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    @Override
    public Integer insert(Log log) {
        return logMapper.insert(log);
    }
}
