package org.promise.test.service.manager.dao;

import lombok.Data;
import org.promise.test.service.mapperService.constant.TestStateEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 18:09
 * @description:
 */
@Data
public class TestDAO {
    private Long testId;

    private Long taskId;

    private Long workerId;

    private TestStateEnum state;//test记录的状态

    private LocalDateTime acceptTime;

    private LocalDateTime finishTime;

    private LocalDateTime cancelTime;

    private List<String> testTools;

    private String operatingSystem;

    private String conclusion;

    private String suggestion;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
