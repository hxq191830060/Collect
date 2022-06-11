package org.promise.publish.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/28 16:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublisherIdResponse implements Serializable {

    private static final long serialVersionUID = 6666610337954137339L;

    private Long publisherId;
}
