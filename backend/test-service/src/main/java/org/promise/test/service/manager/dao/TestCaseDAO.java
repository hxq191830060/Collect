package org.promise.test.service.manager.dao;

import lombok.Data;
import org.promise.test.service.mapperService.constant.DefectLevelEnum;
import org.promise.test.service.mapperService.constant.TestCaseStatusEnum;

import java.time.LocalDateTime;

@Data
public class TestCaseDAO {

    private Long id;

    private Long testId;

    private Long caseId;

    private TestCaseStatusEnum status;

    private String runningTime;

    private DefectLevelEnum defectLevel;

    private String errorMsg;

    private String reproduceSteps;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
