package org.promise.http.service.vo.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.promise.http.service.httpService.dto.TestSimilarityDTO;
import org.promise.test.service.api.info.FinishedTestInfo;
import org.promise.user.service.api.info.UserInfo;

/**
 * @author moqi
 * @date 2022/3/30 0:17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTestVO {

    private static final long serialVersionUID = -587002474906862681L;

    private Long testId;

    private Long workerId;

    //平均得分
    private Double averageRate=0D;

    //协作人数
    private Integer evaluationNumber;

    //该测试完成的时间
    private String finishTime;

    private String avatar;

    private String nickname;

    private Double similarity;


    public SimpleTestVO(@NonNull TestSimilarityDTO testSimilarityDTO,
                        @NonNull FinishedTestInfo testInfo,
                        @NonNull UserInfo userInfo) {
        this.testId = testSimilarityDTO.getReportId();
        this.workerId = testInfo.getWorkerId();
        this.averageRate = testInfo.getAverageEvaluation();
        this.evaluationNumber = testInfo.getCollaborationNumber();
        this.finishTime = testInfo.getFinishTime();
        this.avatar = userInfo.getAvatar();
        this.nickname = userInfo.getNickName();
        this.similarity = testSimilarityDTO.getSim();
    }
}
