package org.promise.publish.service.mapperService.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/2/26 23:11
 */
@Data
public class TaskFilePO {

    private Long fileId;

    private Long taskId;

    private String fileName;

    private String fileUrl;

    private String fileSize;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime fileLastUpdateTime;

    //文件的类型用数字表示，0为应用，1为文档，2为附件
    private Integer fileType;

    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
