package org.promise.test.service.manager.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.test.service.mapperService.constant.DefectLevelEnum;
import org.promise.test.service.mapperService.constant.TestCaseStatusEnum;

import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/4/2 0:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaborationCaseDAO {

    private Long id;//主键id

    private Long collaborationId;//not null

    private Long caseId;

    private TestCaseStatusEnum status;

    private String runningTime;

    private DefectLevelEnum defectLevel;

    private String errorMsg;

    private String reproduceSteps;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
