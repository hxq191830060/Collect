package org.promise.publish.service.api.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/28 13:35
 */
@Data
public class PublisherIdRequest implements Serializable {

    private static final long serialVersionUID = 8209653513650280464L;

    Long userId;
}
