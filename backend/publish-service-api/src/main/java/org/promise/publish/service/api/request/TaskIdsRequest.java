package org.promise.publish.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author moqi
 * @date 2022/3/9 18:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskIdsRequest implements Serializable {

    private static final long serialVersionUID = -7956896977002733914L;

    private List<Long> taskIds;

    private int type;
}
