package org.promise.http.service.vo.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author moqi
 * @date 2022/4/2 10:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollaborationCaseVO {

    private Long id;

    private Long collaborationId;

    private Long caseId;

    private String status;//测试用例状态

    private String runningTime;

    private String defectLevel;//缺陷等级

    private String errorMsg;

    private String reproduceSteps;

}
