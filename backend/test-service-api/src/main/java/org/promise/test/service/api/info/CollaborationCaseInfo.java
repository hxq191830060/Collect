package org.promise.test.service.api.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/4/2 1:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollaborationCaseInfo implements Serializable {

    private static final long serialVersionUID = -3656354512644655440L;

    private Long id;

    private Long collaborationId;

    private Long caseId;

    private String status;//测试用例状态

    private String runningTime;

    private String defectLevel;//缺陷等级

    private String errorMsg;

    private String reproduceSteps;

    private String createTime;

    private String updateTime;

}
