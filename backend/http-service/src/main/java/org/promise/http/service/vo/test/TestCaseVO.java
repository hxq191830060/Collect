package org.promise.http.service.vo.test;

import lombok.Data;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/27 22:57
 */
@Data
public class TestCaseVO implements Serializable {

    private static final long serialVersionUID = 3413348283980099439L;

    private Long id;

    private Long testId;

    private Long caseId;

    private String status; //测试用例状态

    private String runningTime;

    private String defectLevel; //缺陷等级

    private String errorMsg;

    private String reproduceSteps;
}
