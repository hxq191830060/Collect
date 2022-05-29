package org.promise.publish.service.api.info;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/2/26 23:29
 */

@Data
public class TaskFileInfo implements Serializable {

    private static final long serialVersionUID = 6473276653749714093L;

    private Long fileId;

    private Long taskId;

    private String fileName;

    private String fileUrl;

    private String fileSize;

    private String fileLastUpdateTime;

    private Integer fileType;

}
