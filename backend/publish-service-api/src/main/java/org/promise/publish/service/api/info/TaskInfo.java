package org.promise.publish.service.api.info;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author moqi
 * @date 2022/2/27 13:47
 */
@Data
public class TaskInfo implements Serializable {

    private static final long serialVersionUID = -2487164392540446259L;

    private Long taskId;

    private String taskName;

    private Long publisherId;

    private List<TaskFileInfo> taskFileInfoList;

    private String testStartTime;

    private String testEndTime;

    private Integer testWorkerCount;

    private String testType;

    private Integer profit;

    private String allowCancelTime;

    private String requirementDescription;

    private String basicFunction;

    private List<String> taskImgList;

    private List<String> taskSkillList;

    private List<String> taskOsList;

    private Integer currentWorker;
}
