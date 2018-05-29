/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.distribution.id;

import com.google.common.base.Strings;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.util.redis.RedisCacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 用途：基于Redis分布式下全局的ID，保证ID生成的顺序性、无重复性、高可用，
 * 利用Redis排除了受单点故障问题的影响，生成ID规则： 年份（2位） + 一年中第几天 （3位）+ 小时（2位） +
 * Redis自增序号 （6位），总计13位， 支持一小时内近100w个订单号的生成和使用， 比如 1813217008249
 * 作者: lishuyi
 * 时间: 2018/5/12  17:06
 */
@Component
@Slf4j
public class RedisIdGenerator {

    @Value("${server.servlet.contextPath}")
    private String contextPath;

    /**
     * @param date
     * @return 1836517
     */
    private String getDateHourPrefix(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_YEAR); // 今天是第多少天
        int hour =  c.get(Calendar.HOUR_OF_DAY);
        String dayFmt = String.format("%1$03d", day); // 返回一年中第几天，0补位操作 必须满足三位
        String hourFmt = String.format("%1$02d", hour);  //返回一天中第几个小时，0补位操作 必须满足2位
        StringBuffer prefix = new StringBuffer();
        prefix.append((year - 2000)).append(dayFmt).append(hourFmt);
        return prefix.toString();
    }

    /**
     * @Description 支持一个小时100w个订单号的生成
     *
     * @author butterfly
     * @param prefix
     * @return
     */
    private Long incrOrderId(String cacheName, String dateHour) {
        String orderId = null;
        String rediskey = "runtime::"+contextPath+"::#{cacheName}::id::".replace("#{cacheName}", cacheName).concat(dateHour); // 1836517
        try {
            Long index = RedisCacheUtils.incr(rediskey);
            orderId = dateHour.concat(String.format("%1$06d", index)); // 补位操作 保证满足6位
        } catch(Exception ex) {
            log.error("Generate distributited id by redis catch an exception.");
            Exceptions.printException(ex);
        }
        if (Strings.isNullOrEmpty(orderId)) return null;
        return Long.parseLong(orderId);
    }

    public Long generatorId(String cacheName) {
        // 转成数字类型，可排序
        return incrOrderId(cacheName, getDateHourPrefix(new Date()));
    }

    public Long generatorId() {
        // 转成数字类型，可排序
        return incrOrderId("default", getDateHourPrefix(new Date()));
    }
}
