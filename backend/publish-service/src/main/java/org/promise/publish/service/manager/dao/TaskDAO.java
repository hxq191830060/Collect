package org.promise.publish.service.manager.dao;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author moqi
 * @date 2022/2/27 15:59
 */
@Data
public class TaskDAO {

    private Long taskId;

    private String taskName;

    private Long publisherId;

    private List<TaskFileDAO> taskFileDAOList;

    private LocalDateTime testStartTime;

    private LocalDateTime testEndTime;

    private Integer testWorkerCount;

    private String testType;

    private Integer profit;

    private LocalDateTime allowCancelTime;

    private String requirementDescription;

    private String basicFunction;

    private List<String> taskImgList;

    private List<String> taskSkillList;

    private List<String> taskOsList;

    private Integer currentWorker;

    private String avatar;

    private String nickname;
}
