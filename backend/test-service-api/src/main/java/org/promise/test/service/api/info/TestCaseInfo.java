package org.promise.test.service.api.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 17:36
 * @description:
 */
@Data
@NoArgsConstructor
public class TestCaseInfo implements Serializable {
    private static final long serialVersionUID = -2310714408114362376L;

    private Long id;

    private Long testId;

    private Long caseId;

    private String status;//测试用例状态

    private String runningTime;

    private String defectLevel;//缺陷等级

    private String errorMsg;

    private String reproduceSteps;

    private String createTime;

    private String updateTime;


    //only for test
    public TestCaseInfo ( Long caseId, String status, String runningTime, String defectLevel, String errorMsg, String reproduceSteps ) {
        this.caseId = caseId;
        this.status = status;
        this.runningTime = runningTime;
        this.defectLevel = defectLevel;
        this.errorMsg = errorMsg;
        this.reproduceSteps = reproduceSteps;
    }
}
