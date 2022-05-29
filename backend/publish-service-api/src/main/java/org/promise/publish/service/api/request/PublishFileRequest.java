package org.promise.publish.service.api.request;

import lombok.Data;
import org.promise.publish.service.api.info.TaskFileInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author moqi
 * @date 2022/2/27 19:19
 */
@Data
public class PublishFileRequest implements Serializable {

    private static final long serialVersionUID = 1113789646928802535L;

    private List<TaskFileInfo> taskFileInfos;
}
