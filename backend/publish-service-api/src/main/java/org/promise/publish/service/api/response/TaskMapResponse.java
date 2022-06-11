package org.promise.publish.service.api.response;

import lombok.Data;
import org.promise.common.result.page.Page;
import org.promise.publish.service.api.info.TaskInfo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author moqi
 * @date 2022/3/9 18:03
 */
@Data
public class TaskMapResponse implements Serializable {

    private static final long serialVersionUID = 6608008788321736939L;

    Map<Long, TaskInfo> taskInfoMap;
}
