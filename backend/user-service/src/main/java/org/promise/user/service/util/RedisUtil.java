package org.promise.user.service.util;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author moqi
 * @date 2022/5/19 19:28
 */
@Component
public class RedisUtil {

    @Resource
    RedissonClient redissonClient;

    public void setString(String key,String value){
        redissonClient.getBucket(key).set(value);
    }

    public void setString(String key,String value,long exp){
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value);
        bucket.expire(exp,TimeUnit.MILLISECONDS);
    }

    public String getString(String key){
        Object res = redissonClient.getBucket(key).get();
        if(res==null){
            return null;
        }
        assert res instanceof String;
        return (String) res;
    }

    public void delKey(String key){
        RBucket<Object> bucket = redissonClient.getBucket(key);
        if(bucket!=null){
            bucket.delete();
        }
    }
}
