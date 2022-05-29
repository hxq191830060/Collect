package org.promise.publish.service.util.strategy;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author moqi
 * @date 2022/2/28 20:14
 */
@Component
@Slf4j
public class JudgeTimeStrategy {

    ZoneId zoneId=ZoneId.of("Asia/Shanghai");

    Maps.EntryTransformer<java.time.LocalDateTime, java.time.LocalDateTime, Boolean> localDateTimeLocalDateTimeBooleanEntryTransformer0 = (LocalDateTime startTime, LocalDateTime endTime) -> true;

    Maps.EntryTransformer<java.time.LocalDateTime, java.time.LocalDateTime, Boolean> localDateTimeLocalDateTimeBooleanEntryTransformer1 = (LocalDateTime startTime, LocalDateTime endTime) -> {
        assert startTime != null;
        log.info("当前时间为{}",LocalDateTime.now(zoneId));
        if (!LocalDateTime.now(zoneId).isBefore(startTime)) {
            return false;
        }
        assert endTime != null;
        return !startTime.isAfter(endTime);
    };
    Maps.EntryTransformer<java.time.LocalDateTime, java.time.LocalDateTime, Boolean> localDateTimeLocalDateTimeBooleanEntryTransformer2 = (LocalDateTime startTime, LocalDateTime endTime) -> {
        assert startTime != null;
        log.info("当前时间为{}",LocalDateTime.now(zoneId));
        if (!startTime.isBefore(LocalDateTime.now(zoneId))) {
            return false;
        }
        assert endTime != null;
        log.info("当前时间为{}",LocalDateTime.now(zoneId));
        return LocalDateTime.now(zoneId).isBefore(endTime);
    };
    Maps.EntryTransformer<java.time.LocalDateTime, java.time.LocalDateTime, Boolean> localDateTimeLocalDateTimeBooleanEntryTransformer3 = (LocalDateTime startTime, LocalDateTime endTime) -> {

        assert endTime != null;
        log.info("当前时间为{}",LocalDateTime.now(zoneId));
        return endTime.isBefore(LocalDateTime.now(zoneId));
    };

    Maps.EntryTransformer<LocalDateTime, LocalDateTime, Boolean>[] map=new Maps.EntryTransformer[]{
            localDateTimeLocalDateTimeBooleanEntryTransformer0,
            localDateTimeLocalDateTimeBooleanEntryTransformer1,
            localDateTimeLocalDateTimeBooleanEntryTransformer2,
            localDateTimeLocalDateTimeBooleanEntryTransformer3
    };

    public boolean judgeTime(LocalDateTime startTime,LocalDateTime endTime,Integer type){
        return map[type].transformEntry(startTime,endTime);
    }
}
