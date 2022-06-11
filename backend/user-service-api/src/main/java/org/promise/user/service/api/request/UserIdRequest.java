package org.promise.user.service.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/25 20:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdRequest implements Serializable {

    private static final long serialVersionUID = -2434363544367353868L;

    private Long userId;
}
