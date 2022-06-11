package org.promise.publish.service.api.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.promise.publish.service.api.info.TaskInfo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/2/27 15:19
 */
@Data
@AllArgsConstructor
public class TaskResponse implements Serializable {

    private static final long serialVersionUID = -2752622829851769611L;

    private TaskInfo taskInfo;

}
