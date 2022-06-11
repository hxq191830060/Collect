package org.promise.http.service.vo.test;

import lombok.Data;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/27 22:57
 */
@Data
public class TestCollaborationRecordVO implements Serializable {

    private static final long serialVersionUID = 7086882531750701340L;

    private Long id;

    private Long testId;//被评论的测试报告的id

    private Long workerId;//评论者的id

    private Integer rate;//评分

    private String comment;//评价

    private String nickname;

    private String avatar;

    private String createTime;

    private String updateTime;
}
