package org.promise.publish.service.api.response;

import lombok.Data;
import org.promise.common.result.page.Page;
import org.promise.publish.service.api.info.TaskInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author moqi
 * @date 2022/2/27 13:45
 */
@Data
public class
TaskListResponse implements Serializable {

    private static final long serialVersionUID = 6666610337954137339L;

    private List<TaskInfo> taskInfoList;

    private Page page;

    public TaskListResponse(List<TaskInfo> taskInfoList) {
        this.taskInfoList = taskInfoList;
    }

    public TaskListResponse(){

    }
}
