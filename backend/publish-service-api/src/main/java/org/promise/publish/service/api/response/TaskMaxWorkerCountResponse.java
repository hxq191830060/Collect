package org.promise.publish.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/27 23:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskMaxWorkerCountResponse implements Serializable {

    private static final long serialVersionUID = 7881920107433858426L;

    private Integer maxWorkerCount;
}
