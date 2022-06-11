package org.promise.test.service.manager;

import org.promise.test.service.manager.dao.CollaborationCaseDAO;
import org.promise.test.service.manager.dao.CollaborationDAO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 0:58
 */
public interface CollaborationManager {


    CollaborationDAO getCollaborationDAOById(Long id);

    /**
     *
     * @param collaborationDAO 协作报告信息
     * 获得协作报告相关的协作测试用例信息
     */
    List<CollaborationCaseDAO> getCollaborationCaseDAOListByCollaborationDAO(CollaborationDAO collaborationDAO);

    /**
     * @param testId
     * 根据testId获得所有未被删除的协同报告信息
     */
    List<CollaborationDAO> getUsableCollaborationDAOListByTestId(Long testId);



    /**
     * 创建/更新协作报告
     * @param collaborationDAO 协作报告主题信息
     * @param collaborationCaseDAOList 协作报告测试用例信息
     * @return 协作报告新的主键id
     */
    Long submitCollaboration(CollaborationDAO collaborationDAO, List<CollaborationCaseDAO> collaborationCaseDAOList);


    Long whereWorkerHasSubmitCollaboration(Long testId,Long workerId);

}
