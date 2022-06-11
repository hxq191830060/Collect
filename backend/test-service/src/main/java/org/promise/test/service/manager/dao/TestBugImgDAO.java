package org.promise.test.service.manager.dao;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestBugImgDAO {

    private Long bugImgId;

    private Long testId;

    private String imgUrl;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
