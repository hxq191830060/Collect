package org.promise.test.service.api.info;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 17:35
 * @description: 测试中提交的bug图片
 */
@Data
public class TestBugImgInfo implements Serializable {

    private static final long serialVersionUID = -3860660682602689588L;

    private Long bugImgId;

    private Long testId;

    private String imgUrl;

    private String createTime;

    private String updateTime;
}
