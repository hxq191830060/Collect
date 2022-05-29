package org.promise.test.service.manager.impl;

import org.apache.dubbo.common.utils.CollectionUtils;
import org.promise.test.service.convert.TestCollaborationRecordConvert;
import org.promise.test.service.convert.TestConvert;
import org.promise.test.service.manager.TestCollaborationRecordManager;
import org.promise.test.service.manager.dao.TestCollaborationRecordDAO;
import org.promise.test.service.mapperService.TestCollaborationRecordMapper;
import org.promise.test.service.mapperService.po.TestCollaborationRecordPO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
public class TestCollaborationRecordManagerImpl implements TestCollaborationRecordManager {

    @Resource
    private TestCollaborationRecordMapper testCollaborationRecordMapper;

    @Override
    public List<TestCollaborationRecordDAO> getUsableTestCollaborationRecordListByTestId ( Long testId ) {
        List<TestCollaborationRecordPO> poList=testCollaborationRecordMapper.getUsableTestCollaborationRecordByTestId (testId);
        if(CollectionUtils.isEmpty (poList)){
            return Collections.emptyList ();
        }
        return TestCollaborationRecordConvert.INSTANCE.convertPOList2DAOList (poList);
    }

    @Override
    public void submitTestCollaborationRecord ( TestCollaborationRecordDAO testCollaborationRecordDAO ) {
        TestCollaborationRecordPO testCollaborationRecordPO= testCollaborationRecordMapper.getUsableTestCollaborationRecordByTestIdAndWorkerId (testCollaborationRecordDAO.getTestId ( ), testCollaborationRecordDAO.getWorkerId ( ));

        if(testCollaborationRecordPO==null){
            //之前没有评论过，插入一条新评论
            testCollaborationRecordMapper.insert (TestCollaborationRecordConvert.INSTANCE.convertDAO2PO (testCollaborationRecordDAO));
        }else{
            //之前评论过，修改原有评论
            testCollaborationRecordDAO.setId (testCollaborationRecordPO.getId ( ));
            testCollaborationRecordMapper.updateById(TestCollaborationRecordConvert.INSTANCE.convertDAO2PO (testCollaborationRecordDAO));
        }
    }
}
