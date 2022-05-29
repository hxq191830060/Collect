package org.promise.test.service.mapperService.po;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 16:38
 * @description:
 */
@Data
public class TestCountPO {

    private Long id;

    private Long taskId;

    private Integer testNum;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
