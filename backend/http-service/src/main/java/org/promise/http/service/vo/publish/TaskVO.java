package org.promise.http.service.vo.publish;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/2 13:16
 */
@Data
public class TaskVO implements Serializable {

    private static final long serialVersionUID = -8972213027309329128L;

    private Long taskId;

    private String avatar;

    private String nickname;

    private String name;

    private Long publisherId;

    private List<TaskFileVO> taskFileList;

    private String startTime;

    private String endTime;

    private Integer totalWorker;

    private String mode;

    private Integer profit;

    private Integer currentWorker;

    private String cancellationTime;

    private String requestDescription;

    private String baseFunction;

    private List<String> previewShots;

    private List<String> requireSkills;

    private List<String> testEnvironments;
}
