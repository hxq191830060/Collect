package org.promise.test.service.manager.dao;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestCollaborationRecordDAO {

    private Long id;

    private Long testId;

    private Long workerId;

    private Integer evaluation;

    private String recommendation;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
