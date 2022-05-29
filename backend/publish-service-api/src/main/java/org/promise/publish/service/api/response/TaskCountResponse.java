package org.promise.publish.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/28 13:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCountResponse implements Serializable {

    private static final long serialVersionUID = -5791645686261829315L;

    Integer taskCount;
}
