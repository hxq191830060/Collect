package org.promise.test.service.api.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/5/18 21:32
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestSummaryInfo implements Serializable {

    private static final long serialVersionUID = -8270205745088095583L;

    private Long testId;

    private Long workerId;

    //平均得分
    private Double averageEvaluation=0D;

    //协作该报告的人数
    private Integer collaborationNumber;

    private Integer testCaseCount;

    private Integer testToolCount;

    private Integer wrongCaseCount;

    private Integer bugImgCount;

    //结论和建议的总字数
    private Integer wordCount;
}
