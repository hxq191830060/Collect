package org.promise.http.service.util;

import org.apache.commons.lang3.StringUtils;
import org.promise.common.constant.ExceptionEnum;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author moqi
 * @date 2022/3/2 13:05
 */
@Component
public class CommonCheckUtil {

    public void checkNull(Object o){
        if(o==null){
            ExceptionEnum.NullException.maybeThrow();
        }
    }

    public void checkString(String string){
        if(StringUtils.isEmpty(string)){
            ExceptionEnum.NullException.maybeThrow();
        }
    }

    /**
     * 要求Integer不为null，并且不为负数
     */
    public void checkInteger(Integer integer){
        if(integer==null||integer<0){
            ExceptionEnum.NullException.maybeThrow();
        }
    }

    /**
     * 要求Long不为null，并且为正数
     */
    public void checkLong(Long l){
        if(l==null||l<=0){
            ExceptionEnum.NullException.maybeThrow();
        }
    }

    public void checkLocalDateTime(LocalDateTime localDateTime){
        if(localDateTime==null){
            ExceptionEnum.NullException.maybeThrow();
        }
        assert localDateTime != null;
        if(localDateTime.isBefore(LocalDateTime.now())){
            ExceptionEnum.TimeWrongException.maybeThrow();
        }
    }

}
