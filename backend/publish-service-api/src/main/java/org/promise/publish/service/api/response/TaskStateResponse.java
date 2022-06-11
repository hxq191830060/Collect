package org.promise.publish.service.api.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/23 15:17
 */
@Data
public class TaskStateResponse implements Serializable {

    private static final long serialVersionUID = -253751480687024825L;

    boolean submitted;
}
