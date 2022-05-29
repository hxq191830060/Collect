package org.promise.test.service.manager.impl;

import org.promise.test.service.manager.TestCountManager;
import org.promise.test.service.mapperService.TestCountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TestCountManagerImpl implements TestCountManager {

    @Resource
    private TestCountMapper testCountMapper;

    @Override
    public Integer getWorkerNumberByTaskId ( Long taskId ) {
        return testCountMapper.getWorkerNumberByTaskId (taskId);
    }
}
