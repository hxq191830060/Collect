package org.promise.publish.service.api.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/27 23:55
 */
@Data
public class TaskTimeResponse implements Serializable {

    private static final long serialVersionUID = 6697279047082191913L;

    private String allowCancelTime;

    private String testEndTime;
}
