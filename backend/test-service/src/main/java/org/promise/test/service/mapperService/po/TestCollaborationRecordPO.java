package org.promise.test.service.mapperService.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 16:37
 * @description:
 */
@Data
public class TestCollaborationRecordPO {

    private Long id;

    private Long testId;//not null

    private Long workerId;//not null

    private Integer evaluation;//评分,not null

    private String recommendation;//建议,not null

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;//标记是否删除
}
