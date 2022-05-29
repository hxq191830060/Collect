package org.promise.http.service.vo.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @author moqi
 * @date 2022/5/18 22:44
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportScoreVO {

    private Long testId;

    private Double score;

    private Long workerId;

    private String nickname;

    private String avatar;


    public ReportScoreVO(Long testId,Double score,Long workerId){
        this.testId=testId;
        this.score=score;
        this.workerId=workerId;
    }
}
