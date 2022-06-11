package org.promise.publish.service.mapperService.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.promise.publish.service.api.request.PublishRequest;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/2/26 21:34
 */
@Data
public class TaskPO  {

    private Long taskId;

    private String taskName;

    private Long publisherId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime testStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime testEndTime;

    private Integer testWorkerCount;

    private String testType;

    private Integer profit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime allowCancelTime;

    private String requirementDescription;

    private String basicFunction;

    private String taskImgList;

    private String taskSkillList;

    private String taskOsList;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
