package org.promise.http.service.vo.test;

import lombok.Data;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/3 0:01
 */
@Data
public class TestBugImgVO implements Serializable {

    private static final long serialVersionUID = -8228927953353790754L;

    private Long bugImgId;

    private Long testId;

    private String imgUrl;

}
