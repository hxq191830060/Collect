package org.promise.test.service.api.info;

import lombok.Data;

import java.io.Serializable;

@Data
//已完成的测试的信息
public class FinishedTestInfo implements Serializable {
    private static final long serialVersionUID = -587002474906862681L;

    private Long testId;

    private Long workerId;

    //平均得分
    private Double averageEvaluation=0D;

    //协作人数
    private Integer collaborationNumber;

    //该测试完成的时间
    private String finishTime;
}
