package org.promise.test.service.api.response;

import lombok.Data;
import org.promise.test.service.api.info.FinishedTestInfo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author:黄相淇
 * @date:2022年03月31日20:51
 * @description:
 */
@Data
public class FinishedTestInfoMapResponse implements Serializable {

    private Map<Long, FinishedTestInfo> map;
}
