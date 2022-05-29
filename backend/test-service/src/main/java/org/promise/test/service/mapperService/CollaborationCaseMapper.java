package org.promise.test.service.mapperService;

import org.apache.ibatis.annotations.Param;
import org.promise.test.service.mapperService.po.CollaborationCasePO;

import java.util.List;

/**
 * @author moqi
 * @date 2022/4/2 0:53
 */
public interface CollaborationCaseMapper {


    Integer insert( CollaborationCasePO collaborationCasePO );


    Integer insertBatch( List<CollaborationCasePO> collaborationCasePOList );



    Integer deleteAllCollaborationCaseByCollaborationId(Long collaborationId);


    /**
     *
     * @param collaborationId
     * @param deleted
     * 查找匹配collaborationId和deleted的CollaborationCase列表信息
     * 如果deleted为null，那么就不管是否删除，都查出来
     */
    List<CollaborationCasePO> getCollaborationCaseByCollaborationIdAndDeleted(@Param ("collaborationId") Long collaborationId,@Param ("deleted") Integer deleted);

}
