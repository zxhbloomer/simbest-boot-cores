/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.redis;

import com.distributed.lock.redis.RedisReentrantLock;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.util.server.HostUtil;
import com.simbest.boot.util.server.SocketUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 用途：Redis分布式锁
 * 作者: lishuyi
 * 时间: 2018/5/12  15:20
 */
@Slf4j
@Component
@DependsOn(value = "redisCacheUtils")
public class RedisDistributedLocker {
    private String master; //集群主控节点ip

    private JedisPool jedisPool;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Autowired
    private JedisConnectionFactory redisConnectionFactory;

    @Autowired
    private HostUtil hostUtil;

    @Getter
    private String hostAddress;

    @Getter
    private Integer runningPort;
    
    @PostConstruct
    private void init() {
        jedisPool = new JedisPool(jedisPoolConfig, redisConnectionFactory.getHostName(), redisConnectionFactory.getPort(),
                redisConnectionFactory.getTimeout(), redisConnectionFactory.getPassword());

        checkMasterIsMe();
    }

    public boolean checkMasterIsMe() {
        if(StringUtils.isEmpty(hostAddress))
            hostAddress = HostUtil.getHostAddress();
        if(runningPort == null || runningPort.equals(0))
            runningPort = hostUtil.getRunningPort();
        log.debug("My host is {} ", hostAddress);
        log.debug("Cluster master is {} ", RedisCacheUtils.getString("redis_master_ip"));
        log.debug("My host port is {}", runningPort);
        log.debug("Cluster master port is {}", RedisCacheUtils.getBean("redis_master_port", Integer.class));
        log.debug("Check master is me result is {} ", hostAddress.equals(RedisCacheUtils.getString("redis_master_ip")) && runningPort.equals(RedisCacheUtils.getBean("redis_master_port", Integer.class)));
        return hostAddress.equals(RedisCacheUtils.getString("redis_master_ip")) && runningPort.equals(RedisCacheUtils.getBean("redis_master_port", Integer.class));
    }

    /**
     * 每隔五分钟调用的表达式为： "0 0/5 * * * ?" 参考 TaskTriggerDefinition
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void becameMasertIfNotExist() {
        RedisReentrantLock lock = new RedisReentrantLock(jedisPool, "redis_clusert_master_locker");
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                //TODO 获得锁后要做的事
                String masterIp = RedisCacheUtils.getString("redis_master_ip");
                Integer masterPort = RedisCacheUtils.getBean("redis_master_port", Integer.class);
                String myIp = hostAddress;
                Integer myPort = runningPort;
                if (StringUtils.isEmpty(masterIp) || StringUtils.isEmpty(masterPort)) {       //1.没有Master
                    makeMeAsMaster(myIp, myPort);
                    log.trace(String.format("IP: %s on port %s become cluster master...", myIp, myPort));
                }

                boolean masterIsAvailable = SocketUtil.checkHeartConnection(masterIp, Integer.valueOf(masterPort));
                if (!masterIsAvailable) {              //2.Master不可用
                    makeMeAsMaster(myIp, myPort);
                    log.trace(String.format("IP: %s on port %s become cluster master...", myIp, myPort));
                } else {
                    log.trace(String.format("Master is already at IP: %s on port %s ...", masterIp, masterPort));
                }

            } else {
                //TODO 获得锁超时后要做的事
                log.trace("I couldn't get the redis lock...");
            }

        } catch (Exception e) {
            Exceptions.printException(e);
        } finally {
            lock.unlock();
        }
    }

    private void makeMeAsMaster(String myIp, Integer myPort){
        RedisCacheUtils.saveString("redis_master_ip", myIp);   //设置我为Master
        RedisCacheUtils.saveBean("redis_master_port", myPort);
    }



}
