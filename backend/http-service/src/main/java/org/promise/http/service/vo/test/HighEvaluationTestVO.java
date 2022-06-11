package org.promise.http.service.vo.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author moqi
 * @date 2022/3/28 18:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighEvaluationTestVO {

    private Long testId;

    private Long workerId;

    //平均得分
    private Double averageRate;

    //协作人数
    private Integer evaluationNumber;

    private String avatar;

    private String nickname;
}
