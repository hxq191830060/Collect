package org.promise.publish.service.api.request;

import lombok.Data;
import org.promise.publish.service.api.info.TaskInfo;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/2/26 14:59
 */
@Data
public class PublishRequest implements Serializable {


    private static final long serialVersionUID = -6134425130002271317L;

    private TaskInfo taskInfo;
}
