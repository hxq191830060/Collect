package org.promise.test.service.manager.impl;

import org.apache.dubbo.common.utils.CollectionUtils;
import org.promise.test.service.convert.TestBugImgConvert;
import org.promise.test.service.manager.TestBugImgManager;
import org.promise.test.service.manager.dao.TestBugImgDAO;
import org.promise.test.service.mapperService.TestBugImgMapper;
import org.promise.test.service.mapperService.po.TestBugImgPO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class TestBugImgManagerImpl implements TestBugImgManager {

    @Resource
    private TestBugImgMapper testBugImgMapper;

    @Override
    public List<TestBugImgDAO> getUsableBugImgListByTestId ( Long testId ) {
        List<TestBugImgPO> testBugImgPOList=testBugImgMapper.getUsableBugImgByTestId (testId);
        if(CollectionUtils.isEmpty (testBugImgPOList)){
            return Collections.emptyList ( );
        }

        return TestBugImgConvert.INSTANCE.convertPOList2DAOList (testBugImgPOList);
    }
}
