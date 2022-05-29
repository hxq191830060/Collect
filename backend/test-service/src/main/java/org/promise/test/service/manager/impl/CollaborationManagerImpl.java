package org.promise.test.service.manager.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.promise.common.constant.ExceptionEnum;
import org.promise.test.service.convert.CollaborationCaseConvert;
import org.promise.test.service.convert.CollaborationConvert;
import org.promise.test.service.manager.CollaborationManager;
import org.promise.test.service.manager.dao.CollaborationCaseDAO;
import org.promise.test.service.manager.dao.CollaborationDAO;
import org.promise.test.service.mapperService.CollaborationCaseMapper;
import org.promise.test.service.mapperService.CollaborationMapper;
import org.promise.test.service.mapperService.po.CollaborationCasePO;
import org.promise.test.service.mapperService.po.CollaborationPO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 0:58
 */
@Service
@Slf4j
public class CollaborationManagerImpl implements CollaborationManager {

    @Resource
    private CollaborationMapper collaborationMapper;

    @Resource
    private CollaborationCaseMapper collaborationCaseMapper;




    @Override
    public CollaborationDAO getCollaborationDAOById ( Long id ) {
        CollaborationPO collaborationPO=collaborationMapper.getCollaborationListById (id);
        if(collaborationPO==null){
            return null;
        }
        return CollaborationConvert.INSTANCE.convertPO2DAO (collaborationPO);
    }

    @Override
    public List<CollaborationCaseDAO> getCollaborationCaseDAOListByCollaborationDAO ( CollaborationDAO collaborationDAO ) {
        List<CollaborationCasePO> collaborationCasePOList=collaborationCaseMapper.getCollaborationCaseByCollaborationIdAndDeleted (collaborationDAO.getId (),collaborationDAO.getDeleted ());
        if(CollectionUtils.isEmpty (collaborationCasePOList)){
            return Collections.emptyList ();
        }
        return CollaborationCaseConvert.INSTANCE.convertPOList2DAOList (collaborationCasePOList);
    }

    @Override
    public List<CollaborationDAO> getUsableCollaborationDAOListByTestId ( Long testId ) {
        List<CollaborationPO> collaborationPOList=collaborationMapper.getCollaborationListByTestIdAndDeleted (testId,0);
        if(CollectionUtils.isEmpty (collaborationPOList)){
            return Collections.emptyList ();
        }
        return CollaborationConvert.INSTANCE.convertPOList2DAOList (collaborationPOList);
    }


    /**
     *
     * @param collaborationDAO
     * 如果collaborationDAO中包含了id，那么说明这是更新操作，否则是创建操作
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitCollaboration(CollaborationDAO collaborationDAO, List<CollaborationCaseDAO> collaborationCaseDAOList){
        if(collaborationDAO.getId ()!=null){
            //执行删除操作
            collaborationMapper.deleteById (collaborationDAO.getId ());
            collaborationCaseMapper.deleteAllCollaborationCaseByCollaborationId (collaborationDAO.getId ());
        }
        CollaborationPO collaborationPO=CollaborationConvert.INSTANCE.convertDAO2PO (collaborationDAO);
        collaborationPO.setId (null);
        collaborationMapper.insert (collaborationPO);
        Long collaborationId=collaborationPO.getId ();
        if(CollectionUtils.isNotEmpty (collaborationCaseDAOList)){
            List<CollaborationCasePO> collaborationCasePOList=CollaborationCaseConvert.INSTANCE.convertDAOList2POList (collaborationCaseDAOList);
            for(CollaborationCasePO collaborationCasePO:collaborationCasePOList){
                collaborationCasePO.setCollaborationId (collaborationId);
            }
            collaborationCaseMapper.insertBatch (collaborationCasePOList);
        }
        return collaborationId;
    }

    @Override
    public Long whereWorkerHasSubmitCollaboration ( Long testId, Long workerId ) {
        List<Long> idList=collaborationMapper.getIdListByTestIdAndWorkerIdAndDeleted (testId,workerId,0);
        if(CollectionUtils.isEmpty (idList)){
            return null;
        }
        if(idList.size ()>1){
            log.error ("数据库中存在{}条未被删除的testId为{},workerId为{} 的collaboration记录,请处理!",idList.size (),testId,workerId);
            ExceptionEnum.DataDuplicateException.maybeThrow ();
        }
        return idList.get (0);
    }
}
