package org.promise.publish.service.api.request;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.promise.common.result.page.Page;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/2/27 13:44
 */
@Data
public class UserIdAndPageRequest implements Serializable {

    private static final long serialVersionUID = 2054336228260555218L;

    private Long userId;

    //0为查询所有，1为查询未开始，2为查询已开始但未结束，3为查询已结束
    private Integer judgeType;

    private Page page;
}
