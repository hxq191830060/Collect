package org.promise.http.service.vo.publish;

import lombok.Data;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/3/2 14:09
 */
@Data
public class TaskFileVO implements Serializable {

    private static final long serialVersionUID = -6927365595494280859L;

    private Long fileId;

    private Long taskId;

    private String name;

    private String url;

    private String size;

    private String uploadTime;

    private Integer fileType;


}
