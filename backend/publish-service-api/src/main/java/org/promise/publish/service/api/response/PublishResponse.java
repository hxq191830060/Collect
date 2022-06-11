package org.promise.publish.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishResponse implements Serializable {

    private static final long serialVersionUID = -3780965486383182497L;

    private Long taskId;
}
