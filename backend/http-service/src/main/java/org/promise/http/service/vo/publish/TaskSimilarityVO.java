package org.promise.http.service.vo.publish;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.promise.http.service.httpService.dto.TaskSimilarityDTO;
import org.promise.publish.service.api.info.TaskInfo;
import org.promise.user.service.api.info.UserInfo;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/31 22:42
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSimilarityVO implements Serializable {

    private static final long serialVersionUID = -8806403588587317553L;

    private Long taskId;

    private String avatar;

    private String nickname;

    private String name;

    private Long publisherId;

    private String startTime;

    private String endTime;

    private Integer totalWorker;

    private String mode;

    private Integer profit;

    private Integer currentWorker;

    private String cancellationTime;

    private String requestDescription;

    private String baseFunction;

    private Double similarity;

    public TaskSimilarityVO(@NonNull TaskSimilarityDTO taskSimilarityDTO,
                            @NonNull TaskInfo taskInfo,
                            @NonNull UserInfo userInfo) {
        this.taskId = taskSimilarityDTO.getTaskId();
        this.avatar = userInfo.getAvatar();
        this.nickname = userInfo.getNickName();
        this.name = taskInfo.getTaskName();
        this.publisherId = taskInfo.getPublisherId();
        this.startTime = taskInfo.getTestStartTime();
        this.endTime = taskInfo.getTestEndTime();
        this.totalWorker = taskInfo.getTestWorkerCount();
        this.mode = taskInfo.getTestType();
        this.profit = taskInfo.getProfit();
        this.currentWorker = taskInfo.getCurrentWorker();
        this.cancellationTime = taskInfo.getAllowCancelTime();
        this.requestDescription = taskInfo.getRequirementDescription();
        this.baseFunction = taskInfo.getBasicFunction();
        this.similarity = taskSimilarityDTO.getSim();
    }
}
