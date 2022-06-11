package org.promise.user.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.promise.user.service.manager.UserManager;
import org.promise.user.service.mapperService.UserMapper;
import org.promise.user.service.mapperService.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author: 黄相淇
 * @date: 2022年03月07日 23:15
 * @description:
 */
@SpringBootTest
@Slf4j
@ExtendWith(SpringExtension.class)
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect(){
        log.info ("{}",userMapper.selectUserByUserName ("moqi@moqi.com"));
        log.info ("{}",userMapper.selectUserByUserId (1L));
        log.info ("{}",userMapper.getUserInfoListByUserIdList (Lists.list (242L,243L,244L )));
    }


}
