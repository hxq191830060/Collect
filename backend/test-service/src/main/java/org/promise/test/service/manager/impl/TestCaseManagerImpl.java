package org.promise.test.service.manager.impl;

import org.apache.dubbo.common.utils.CollectionUtils;
import org.promise.test.service.convert.TestCaseConvert;
import org.promise.test.service.manager.TestCaseManager;
import org.promise.test.service.manager.dao.TestCaseDAO;
import org.promise.test.service.mapperService.TestCaseMapper;
import org.promise.test.service.mapperService.po.TestCasePO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class TestCaseManagerImpl implements TestCaseManager {

    @Resource
    private TestCaseMapper testCaseMapper;

    @Override
    public List<TestCaseDAO> getUsableTestCaseListByTestId ( Long testId ) {
        List<TestCasePO> testCasePOList= testCaseMapper.getUsableTestCaseByTestId (testId);
        if(CollectionUtils.isEmpty (testCasePOList)){
            return Collections.emptyList ();
        }
        return TestCaseConvert.INSTANCE.convertPOList2DAOList (testCasePOList);
    }
}
