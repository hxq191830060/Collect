package org.promise.test.service.api.info;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 17:38
 * @description:
 */
@Data
public class TestCollaborationRecordInfo implements Serializable {
    private static final long serialVersionUID = -2549741055987455712L;

    private Long id;

    private Long testId;//被评论的测试报告的id

    private Long workerId;//评论者的id

    private Integer evaluation;//评分

    private String recommendation;//评价

    private String createTime;

    private String updateTime;
}
