package org.promise.publish.service.api.request;

import lombok.Data;
import org.promise.common.result.page.Page;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/2 14:45
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -4289353668180404568L;

    private Page page;
}
