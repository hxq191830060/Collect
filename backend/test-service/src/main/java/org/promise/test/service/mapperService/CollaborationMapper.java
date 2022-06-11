package org.promise.test.service.mapperService;

import org.apache.ibatis.annotations.Param;
import org.promise.test.service.mapperService.po.CollaborationPO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 0:43
 */
public interface CollaborationMapper {

    Integer insert(CollaborationPO collaborationPO);


    //根据主键查，不管是否删除，都查出来
    CollaborationPO getCollaborationListById(Long id);

    /**
     *
     * @param testId
     * @param deleted
     * 查询符合testId和deleted的记录信息(deleted为0，表示未删除，deleted为1，表示删除）
     * 如果deleted为null，那么不管是否删除都要查出来
     */
    List<CollaborationPO> getCollaborationListByTestIdAndDeleted(@Param("testId")Long testId,@Param ("deleted")Integer deleted);


    Integer deleteById(Long id);


    List<Long> getIdListByTestIdAndWorkerIdAndDeleted(@Param ("testId")Long testId,@Param ("workerId")Long workerId,@Param ("deleted")Integer deleted);

}
