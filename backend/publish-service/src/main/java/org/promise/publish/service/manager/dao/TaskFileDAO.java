package org.promise.publish.service.manager.dao;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/2/27 16:00
 */
@Data
public class TaskFileDAO {

    private Long fileId;

    private Long taskId;

    private String fileName;

    private String fileUrl;

    private String fileSize;

    private LocalDateTime fileLastUpdateTime;

    private String fileType;

}
