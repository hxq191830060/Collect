package org.promise.test.service.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promise.test.service.api.info.ReportIntegrationInfo;

import java.io.Serializable;

/**
 * @author moqi
 * @date 2022/5/19 14:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportIntegrationResponse implements Serializable {

    private static final long serialVersionUID = -5172293109408210122L;

    private ReportIntegrationInfo reportIntegrationInfo;
}
