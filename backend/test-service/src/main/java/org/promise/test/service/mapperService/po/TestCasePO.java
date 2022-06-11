package org.promise.test.service.mapperService.po;

import lombok.Data;
import org.promise.test.service.mapperService.constant.DefectLevelEnum;
import org.promise.test.service.mapperService.constant.TestCaseStatusEnum;

import java.time.LocalDateTime;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 15:55
 * @description:
 */
@Data
public class TestCasePO {

    private Long id;//主键id

    private Long testId;//not null

    private Long caseId;//not null

    private TestCaseStatusEnum status;//not null,测试用例状态

    private String runningTime;//not null,运行时间

    private DefectLevelEnum defectLevel;//缺陷等级，只有status为failed时，才有该字段

    private String errorMsg;//错误信息，只有status为failed时，才有该字段

    private String reproduceSteps;//错误复现步骤,只有status为failed时，才有该字段

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;//标记是否删除

}
