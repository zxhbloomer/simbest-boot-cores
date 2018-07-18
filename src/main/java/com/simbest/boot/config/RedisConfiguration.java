/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import com.google.common.collect.Maps;
import com.simbest.boot.component.distributed.lock.DistributedLockFactoryBean;
import com.simbest.boot.constants.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 用途：Redis 配置信息
 * 作者: lishuyi
 * 时间: 2018/5/1  18:56
 */
@Configuration
@EnableCaching
@EnableRedisHttpSession
@Slf4j
public class RedisConfiguration extends CachingConfigurerSupport {

    @Autowired
    private Environment env;

    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;

    @Value("${spring.redis.cluster.password}")
    private String password;

    @Value("${spring.redis.cluster.max-redirects}")
    private String maxRedirects;

    @Value("${server.servlet.session.timeout}")
    private Integer maxInactiveIntervalInSeconds;

    @Value("${spring.session.redis.namespace}")
    private String redisNamespace;

    @Autowired
    private RedisKeyGenerator redisKeyGenerator;

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    @PostConstruct
    private void afterPropertiesSet() {
        log.info("setting spring session with redis timeout {} seconds", maxInactiveIntervalInSeconds);
        sessionRepository.setDefaultMaxInactiveInterval(maxInactiveIntervalInSeconds);
        // 注释以下代码，配合RedisSessionConfiguration的CookiePath=/可以实现应用直接Cookie共享Session
//        log.info("setting spring session with redis namespace {} ", redisNamespace);
//        sessionRepository.setRedisKeyNamespace(redisNamespace);
    }

//    @Bean
//    public JedisPoolConfig jedisPoolConfig() {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(100);
//        poolConfig.setTestOnBorrow(true);
//        poolConfig.setTestOnReturn(true);
//        return poolConfig;
//    }

    /**
     * @see RedisClusterConfiguration
     * @return
     */
    @Bean
    public RedisClusterConfiguration redisClusterConfiguration(){
        Map<String, Object> source = Maps.newHashMap();
        source.put("spring.redis.cluster.nodes", clusterNodes);
        log.debug("Redis cluster nodes: {}", clusterNodes);
        source.put("spring.redis.cluster.max-redirects", maxRedirects);
        log.info("Redis cluster max redirects: {}", maxRedirects);
        return new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
    }

//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        JedisConnectionFactory factory;
//        if (clusterNodes.split(ApplicationConstants.COMMA).length == 1) {
//            factory = new JedisConnectionFactory(jedisPoolConfig());
//            factory.setHostName(clusterNodes.split(ApplicationConstants.COLON)[0]);
//            factory.setPort(Integer.valueOf(clusterNodes.split(ApplicationConstants.COLON)[1]));
//        } else {
//            factory = new JedisConnectionFactory(redisClusterConfiguration(), jedisPoolConfig());
//        }
//        factory.setPassword(password);
//        factory.setUsePool(true);
//        return factory;
//    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory;
        if (clusterNodes.split(ApplicationConstants.COMMA).length == 1) {
            RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
            standaloneConfig.setHostName(clusterNodes.split(ApplicationConstants.COLON)[0]);
            standaloneConfig.setPort(Integer.valueOf(clusterNodes.split(ApplicationConstants.COLON)[1]));
            standaloneConfig.setPassword(RedisPassword.of(password));
            standaloneConfig.setDatabase(0);
            factory = new LettuceConnectionFactory(standaloneConfig);
        } else {
            RedisClusterConfiguration clusterConfig = redisClusterConfiguration();
            clusterConfig.setPassword(RedisPassword.of(password));
            factory = new LettuceConnectionFactory(clusterConfig);
        }
        return factory;
    }

//    @Bean
//    public RedisCacheConfiguration redisCacheConfiguration() {
//        return RedisCacheConfiguration
//                .defaultCacheConfig()
//                .serializeKeysWith(
//                        RedisSerializationContext
//                                .SerializationPair
//                                .fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.
//                                SerializationPair.
//                                fromSerializer(new JdkSerializationRedisSerializer(this.getClass().getClassLoader())))
//                //默认1小时超时
//                .entryTtl(Duration.ofSeconds(3600));
//    }


    @Bean
    @Override
    public CacheManager cacheManager() {
        // 初始化一个RedisCacheWriter
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory());
        // 设置默认过期时间：60 分钟
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(maxInactiveIntervalInSeconds))
                //.prefixKeysWith("cache:key:uums:") //无法区分不同对象相同id时的key
                // .disableCachingNullValues()
                // 使用注解时的序列化、反序列化
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        Map<String, RedisCacheConfiguration> initialCacheConfigurations = new HashMap<>();
        return new RedisCacheManager(cacheWriter, defaultCacheConfig, initialCacheConfigurations);
    }

    @Bean
    @Qualifier("redisTemplate")
    public <T> RedisTemplate<String, T> redisTemplate() {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setHashValueSerializer(new JdkSerializationRedisSerializer());
        template.setDefaultSerializer(new JdkSerializationRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 自定义Key生成策略
     * @return
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return redisKeyGenerator;
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            /**
             * 从缓存读取数据报错时，不作处理，由数据库提供服务
             * @param e
             * @param cache
             * @param key
             */
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                if(e instanceof RedisConnectionFailureException){
                    log.warn("redis has lose connection:",e);
                    return;
                }
                throw e;
            }

            /**
             * 向缓存写入数据报错时，不作处理，由数据库提供服务
             * @param e
             * @param cache
             * @param key
             * @param value
             */
            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                if(e instanceof RedisConnectionFailureException){
                    log.warn("redis has lose connection:",e);
                    return;
                }
                throw e;
            }

            /**
             * 删除缓存报错时，抛出异常
             * @param e
             * @param cache
             * @param key
             */
            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.error("handleCacheEvictError缓存时异常---key：-"+key+"异常信息:"+e);
                throw e;
            }

            /**
             * 清理缓存报错时，抛出异常
             * @param e
             * @param cache
             */
            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("清除缓存时异常---：-"+"异常信息:"+e);
                throw e;
            }
        };
        return cacheErrorHandler;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        if (clusterNodes.split(ApplicationConstants.COMMA).length == 1) {
            config.useSingleServer().setAddress("redis://"+clusterNodes)
            .setPassword(password);
        } else {
            String[] nodes = clusterNodes.split(ApplicationConstants.COMMA);
            for(int i=0; i<nodes.length; i++){
                nodes[i] = "redis://"+ nodes[i];
            }
            config.useClusterServers()
                    .setScanInterval(2000) // cluster state scan interval in milliseconds
                    .setPassword(password)
                    .addNodeAddress(nodes);
//                    .addNodeAddress("redis://10.92.80.70:26379", "redis://10.92.80.70:26389", "redis://10.92.80.70:26399")
//                    .addNodeAddress("redis://10.92.80.71:26379", "redis://10.92.80.71:26389", "redis://10.92.80.71:26399");
        }
        return Redisson.create(config);
    }

    @Bean
    @DependsOn("redissonClient")
    public DistributedLockFactoryBean distributeLockTemplate(){
        DistributedLockFactoryBean d = new DistributedLockFactoryBean();
        d.setMode("SINGLE");
        return d;
    }


}

