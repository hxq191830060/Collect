package org.promise.test.service.mapperService.po;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author: 黄相淇
 * @date: 2022年03月24日 15:53
 * @description:
 */
@Data
public class TestBugImgPO {

    private Long bugImgId;//主键

    private Long testId;//对应的测试的testId，not null

    private String imgUrl;//bug_img的url,not null

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;//标记是否删除
}
