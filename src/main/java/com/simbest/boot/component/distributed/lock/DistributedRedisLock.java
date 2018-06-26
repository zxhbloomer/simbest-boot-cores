/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.component.distributed.lock;

import com.simbest.boot.constants.ApplicationConstants;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 用途：
 * 作者: lishuyi
 * 时间: 2018/6/22  17:40
 */
@Component
public class DistributedRedisLock {
    
    @Autowired
    private RedissonClient redisson;

    @Value("${spring.cache.redis.key-prefix}")
    private String keyPrefix;

    private static String prefix;

    private static DistributedRedisLock lockUtils;

    @PostConstruct
    public void init() {
        lockUtils = this;
        lockUtils.redisson = this.redisson;
        lockUtils.prefix = this.keyPrefix;
    }

    /**
     * 获取锁
     * @param lockName
     */
    public static void tryLock(String lockName){
        String key = prefix + lockName;
        RLock mylock = lockUtils.redisson.getLock(key);
        mylock.lock(ApplicationConstants.REDIS_LOCK_DEFAULT_TIMEOUT, ApplicationConstants.REDIS_LOCK_DEFAULT_TIME_UNIT); //lock提供带timeout参数，timeout结束强制解锁，防止死锁
    }

    /**
     * 释放锁
     * @param lockName
     */
    public static void unlock(String lockName){
        String key = prefix + lockName;
        RLock mylock = lockUtils.redisson.getLock(key);
        mylock.unlock();
    }

    /**
     * 获取锁
     * @param lockName
     */
    public static void tryLock(String lockName, int seconds){
        String key = prefix + lockName;
        RLock mylock = lockUtils.redisson.getLock(key);
        mylock.lock(seconds, ApplicationConstants.REDIS_LOCK_DEFAULT_TIME_UNIT); //lock提供带timeout参数，timeout结束强制解锁，防止死锁
    }

}
