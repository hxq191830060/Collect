package org.promise.user.service.api.response;

import lombok.Data;
import org.promise.user.service.api.info.UserInfo;

import java.io.Serializable;
import java.util.Map;

/**
 * @author: 黄相淇
 * @date: 2022年03月07日 15:57
 * @description:
 */
@Data
public class BatchGetUserInfoResponse implements Serializable {

    private static final long serialVersionUID = 3769782873723104702L;

    private Map<Long, UserInfo> userInfos;

}
