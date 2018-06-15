/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.redis;

import com.simbest.boot.util.json.JacksonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用途：Redis客户端工具类
 * 作者: lishuyi
 * 时间: 2018/5/1  19:04
 */
@Component
public class RedisCacheUtils {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static RedisCacheUtils cacheUtils;

    @Value("${spring.cache.redis.key-prefix}")
    private String keyPrefix;

    private static String prefix;

    @PostConstruct
    public void init() {
        cacheUtils = this;
        cacheUtils.redisTemplate = this.redisTemplate;
        cacheUtils.prefix = this.keyPrefix;
    }

    /**
     * 将数据存入缓存
     *
     * @param prefix + key
     * @param val
     * @return
     */
    public static void saveString(String  key, String val) {
        ValueOperations<String, String> vo = cacheUtils.redisTemplate.opsForValue();
        vo.set(prefix + key, val);
    }

    /**
     * 从缓存中取得字符串数据
     *
     * @param prefix + key
     * @return 数据
     */
    public static String getString(String  key) {
        return cacheUtils.redisTemplate.opsForValue().get(prefix + key);
    }

    /**
     * 将数据存入缓存的集合中
     *
     * @param prefix + key
     * @param val
     * @return
     */
    public static void saveToSet(String  key, String val) {
        SetOperations<String, String> so = cacheUtils.redisTemplate.opsForSet();
        so.add(prefix + key, val);
    }

    /**
     *
     *
     * @param prefix + key
     *            缓存Key
     * @return prefix + keyValue
     * @author:mijp
     * @since:2017/1/16 13:23
     */
    public static String getFromSet(String  key) {
        return cacheUtils.redisTemplate.opsForSet().pop(prefix + key);
    }

    /**
     * 将 prefix + key的值保存为 value ，当且仅当 prefix + key 不存在。 若给定的 prefix + key 已经存在，则 SETNX 不做任何动作。 SETNX 是『SET
     * if Not eXists』(如果不存在，则 SET)的简写。 <br>
     * 保存成功，返回 true <br>
     * 保存失败，返回 false
     */
    public static boolean saveNX(String  key, String val) {
        /** 设置成功，返回 1 设置失败，返回 0 **/
        return cacheUtils.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            return connection.setNX((prefix + key).getBytes(), val.getBytes());
        });

    }

    /**
     * 将 prefix + key的值保存为 value ，当且仅当 prefix + key 不存在。 若给定的 prefix + key 已经存在，则 SETNX 不做任何动作。 SETNX 是『SET
     * if Not eXists』(如果不存在，则 SET)的简写。 <br>
     * 保存成功，返回 true <br>
     * 保存失败，返回 false
     *
     * @param prefix + key
     * @param val
     * @param expire
     *            超时时间
     * @return 保存成功，返回 true 否则返回 false
     */
    public static boolean saveNX(String  key, String val, int expire) {
        boolean ret = saveNX(prefix + key, val);
        if (ret) {
            cacheUtils.redisTemplate.expire(prefix + key, expire, TimeUnit.SECONDS);
        }
        return ret;
    }

    /**
     * 将数据存入缓存（并设置失效时间）
     *
     * @param prefix + key
     * @param val
     * @param seconds
     * @return
     */
    public static void saveString(String  key, String val, int seconds) {
        cacheUtils.redisTemplate.opsForValue().set(prefix + key, val, seconds, TimeUnit.SECONDS);
    }

    /**
     * 保存复杂类型数据到缓存
     *
     * @param prefix + key
     * @param obj
     * @return
     */
    public static void saveBean(String  key, Object obj) {
        cacheUtils.redisTemplate.opsForValue().set(prefix + key, JacksonUtils.obj2json(obj));
    }

    /**
     * 保存复杂类型数据到缓存（并设置失效时间）
     *
     * @param prefix + key
     * @param Object
     * @param seconds
     * @return
     */
    public static void saveBean(String  key, Object obj, int seconds) {
        cacheUtils.redisTemplate.opsForValue().set(prefix + key, JacksonUtils.obj2json(obj), seconds, TimeUnit.SECONDS);
    }

    /**
     * 取得复杂类型数据
     *
     * @param prefix + key
     * @param obj
     * @param clazz
     * @return
     */
    public static <T> T getBean(String  key, Class<T> clazz) {
        String value = cacheUtils.redisTemplate.opsForValue().get(prefix + key);
        if (value == null) {
            return null;
        }
        return JacksonUtils.json2obj(value, clazz);
    }

    /**
     * 功能: 存到指定的队列中<br />
     * 左近右出
     *
     * @param prefix + key
     * @param val
     * @param size
     *            队列大小限制 0：不限制
     */
    public static void saveToQueue(String  key, String val, long size) {
        ListOperations<String, String> lo = cacheUtils.redisTemplate.opsForList();

        if (size > 0 && lo.size(prefix + key) >= size) {
            lo.rightPop(prefix + key);
        }
        lo.leftPush(prefix + key, val);
    }

    /**
     *
     * 功能: 从指定队列里取得数据<br />
     *
     * @param prefix + key
     * @param size
     *            数据长度
     * @return
     */
    public static List<String> getFromQueue(String  key, long size) {
        boolean flag = cacheUtils.redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            return connection.exists((prefix + key).getBytes());
        });

        if (flag) {
            return new ArrayList<>();
        }
        ListOperations<String, String> lo = cacheUtils.redisTemplate.opsForList();
        if (size > 0) {
            return lo.range(prefix + key, 0, size - 1);
        } else {
            return lo.range(prefix + key, 0, lo.size(prefix + key) - 1);
        }
    }

    /**
     * 保存到hash集合中
     *
     * @param hName
     *            集合名
     * @param prefix + key
     * @param val
     */
    public static void hashSet(String key, String  hashkey, String value) {
        cacheUtils.redisTemplate.opsForHash().put(prefix + key, hashkey, value);
    }

    /**
     * 根据prefix + key获取所以值
     *
     * @param prefix + key
     * @return
     */
    public static Map<Object, Object> hgetAll(String  key) {
        return cacheUtils.redisTemplate.opsForHash().entries(prefix + key);
    }

    /**
     * 保存到hash集合中
     *
     * @param <T>
     *
     * @param hName
     *            集合名
     * @param prefix + key
     * @param val
     */
    public static <T> void hashSet(String key, String  hashkey, T t) {
        hashSet(prefix + key, hashkey, JacksonUtils.obj2json(t));
    }

    /**
     *
     * 功能: 从指定队列里取得数据
     *
     * @param prefix + key
     * @return
     */
    public static String popQueue(String  key) {
        return cacheUtils.redisTemplate.opsForList().rightPop(prefix + key);

    }

    public static byte[] get(String  key) {
        return cacheUtils.redisTemplate.execute((RedisCallback<byte[]>) connection ->
                connection.get((prefix +key).getBytes()));
    }

//
//    /**
//     * 将自增变量存入缓存
//     */
//    public static void saveSeq(String  key, long seqNo) {
//        cacheUtils.redisTemplate.delete(prefix + key);
//        cacheUtils.redisTemplate.opsForValue().increment(prefix + key, seqNo);
//    }

    /**
     * 取得序列值的下一个
     *
     * @param prefix + key
     * @return
     */
    public static Long seqNext(String  key) {
        return cacheUtils.redisTemplate.execute((RedisCallback<Long>) connection ->
                connection.incr((prefix + key).getBytes()));
    }

    /**
     * 取得序列值的下一个
     *
     * @param prefix + key
     * @return
     */
    public static Long seqNext(String  key, long value) {
        return cacheUtils.redisTemplate.execute((RedisCallback<Long>) connection ->
                connection.incrBy((prefix + key).getBytes(), value));

    }

    /**
     * 将序列值回退一个
     *
     * @param prefix + key
     * @return
     */
    public static Long seqBack(String  key) {
        return cacheUtils.redisTemplate.execute((RedisCallback<Long>) connection -> connection.decr((prefix + key).getBytes()));
    }

    /**
     * 将序列值回退一个
     *
     * @param prefix + key
     * @return
     */
    public static Long seqBack(String  key, long value) {
        return cacheUtils.redisTemplate.execute((RedisCallback<Long>) connection -> connection.decrBy((prefix + key).getBytes(), value));
    }

    /**
     * 从hash集合里取得
     *
     * @param hName
     * @param prefix + key
     * @return
     */
    public static Object hashGet(String hName, String  key) {
        return cacheUtils.redisTemplate.opsForHash().get(hName, prefix + key);
    }

    public static <T> T hashGet(String hName, String  key, Class<T> clazz) {
        return JacksonUtils.json2obj((String) hashGet(hName, prefix + key), clazz);
    }

    /**
     * 判断是否缓存了数据
     *
     * @param prefix + key
     *            数据KEY
     * @return 判断是否缓存了
     */
    public static boolean isCached(String  key) {
        return cacheUtils.redisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.exists((prefix + key).getBytes()));
    }

    /**
     * 判断hash集合中是否缓存了数据
     *
     * @param hName
     * @param prefix + key
     *            数据KEY
     * @return 判断是否缓存了
     */
    public static boolean hashCached(String hName, String  key) {
        return cacheUtils.redisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.hExists((prefix + key).getBytes(), (prefix + key).getBytes()));
    }

    /**
     * 判断是否缓存在指定的集合中
     *
     * @param prefix + key
     *            数据KEY
     * @param val
     *            数据
     * @return 判断是否缓存了
     */
    public static boolean isMember(String  key, String val) {
        return cacheUtils.redisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.sIsMember((prefix + key).getBytes(), val.getBytes()));
    }

    /**
     * 从缓存中删除数据
     *
     * @param string
     * @return
     */
    public static Boolean delKey(String  key) {
        //cacheUtils.redisTemplate.execute((RedisCallback<Long>) connection -> connection.del((prefix + key).getBytes()));
        return cacheUtils.redisTemplate.delete(prefix + key);
    }

    public static Long deleteByPrefix(String prex) {
        Set<String>  keys = cacheUtils.redisTemplate.keys(prex+"*");
        return cacheUtils.redisTemplate.delete(keys);
    }

    public static Long deleteBySuffix(String suffix) {
        Set<String> keys = cacheUtils.redisTemplate.keys("*"+suffix);
        return cacheUtils.redisTemplate.delete(keys);
    }

    public void deleteByKeys(String... keys) {
        cacheUtils.redisTemplate.delete(Arrays.asList(prefix + keys));
    }

    /**
     * 设置超时时间
     *
     * @param prefix + key
     * @param seconds
     */
    public static void expire(String  key, int seconds) {
        cacheUtils.redisTemplate
                .execute((RedisCallback<Boolean>) connection -> connection.expire((prefix + key).getBytes(), seconds));

    }

    /**
     * 列出set中所有成员
     *
     * @param setName
     *            set名
     * @return
     */
    public static Set<Object> listSet(String setName) {
        return cacheUtils.redisTemplate.opsForHash().keys(prefix + setName);

    }

    /**
     * 向set中追加一个值
     *
     * @param setName
     *            set名
     * @param value
     */
    public static void setSave(String setName, String value) {
        cacheUtils.redisTemplate
                .execute((RedisCallback<Long>) connection -> connection.sAdd((prefix + setName).getBytes(), value.getBytes()));
    }

    /**
     * 逆序列出sorted set包括分数的set列表
     *
     * @param prefix + key
     *            set名
     * @param start
     *            开始位置
     * @param end
     *            结束位置
     * @return 列表
     */
    public static Set<Tuple> listSortedsetRev(String  key, int start, int end) {
        return cacheUtils.redisTemplate.execute((RedisCallback<Set<Tuple>>) connection ->
                connection.zRevRangeWithScores((prefix + key).getBytes(), start, end));
    }

    /**
     * 逆序取得sorted sort排名
     *
     * @param prefix + key
     *            set名
     * @param member
     *            成员名
     * @return 排名
     */
    public static Long getRankRev(String  key, String member) {
        return cacheUtils.redisTemplate.execute((RedisCallback<Long>) connection -> {
            return connection.zRevRank((prefix + key).getBytes(), member.getBytes());
        });

    }

    /**
     * 根据成员名取得sorted sort分数
     *
     * @param prefix + key
     *            set名
     * @param member
     *            成员名
     * @return 分数
     */
    public static Double getMemberScore(String  key, String member) {
        return cacheUtils.redisTemplate.execute((RedisCallback<Double>) connection -> {
            return connection.zScore((prefix + key).getBytes(), member.getBytes());
        });
    }

    /**
     * 向sorted set中追加一个值
     *
     * @param prefix + key
     *            set名
     * @param score
     *            分数
     * @param member
     *            成员名称
     */
    public static void saveToSortedset(String  key, Double score, String member) {
        cacheUtils.redisTemplate.execute(
                (RedisCallback<Boolean>) connection -> connection.zAdd((prefix + key).getBytes(), score, member.getBytes()));
    }

    /**
     * 从sorted set删除一个值
     *
     * @param prefix + key
     *            set名
     * @param member
     *            成员名称
     */
    public static void delFromSortedset(String  key, String member) {
        cacheUtils.redisTemplate
                .execute((RedisCallback<Long>) connection -> connection.zRem((prefix + key).getBytes(), member.getBytes()));

    }

    /**
     * 从hashmap中删除一个值
     *
     * @param prefix + key
     *            map名
     * @param field
     *            成员名称
     */
    public static void delFromMap(String  key, String field) {
        cacheUtils.redisTemplate
                .execute((RedisCallback<Long>) connection -> connection.hDel((prefix + key).getBytes(), field.getBytes()));

    }



    /**
     *
     * @Description: 根据prefix + key获取当前计数结果
     * @author clg
     * @date 2016年6月30日 下午2:38:20
     *
     * @param prefix + key
     * @return
     */
    public static String getCount(String  key) {
        return cacheUtils.redisTemplate.opsForValue().get(prefix + key);
    }

    /**
     * 将所有指定的值插入到存于 prefix + key 的列表的头部。如果 prefix + key 不存在，那么在进行 push 操作前会创建一个空列表
     *
     * @param <T>
     *
     * @param prefix + key
     * @param value
     * @return
     */
    public static <T> Long lpush(String  key, T value) {
        return cacheUtils.redisTemplate.opsForList().leftPush(prefix + key, JacksonUtils.obj2json(value));
    }

    /**
     * 只有当 prefix + key 已经存在并且存着一个 list 的时候，在这个 prefix + key 下面的 list 的头部插入 value。 与 LPUSH 相反，当
     * prefix + key 不存在的时候不会进行任何操作
     *
     * @param prefix + key
     * @param value
     * @return
     */
    public static <T> Long lpushx(String  key, T value) {
        return cacheUtils.redisTemplate.opsForList().leftPushIfPresent(prefix + key, JacksonUtils.obj2json(value));
    }

    /**
     * 返回存储在 prefix + key 里的list的长度。 如果 prefix + key 不存在，那么就被看作是空list，并且返回长度为 0
     *
     * @param prefix + key
     * @return
     */
    public static Long llen(String  key) {
        return cacheUtils.redisTemplate.opsForList().size(prefix + key);
    }

    /**
     * 返回存储在 prefix + key 的列表里指定范围内的元素。 start 和 end
     * 偏移量都是基于0的下标，即list的第一个元素下标是0（list的表头），第二个元素下标是1，以此类推
     *
     * @param prefix + key
     * @return
     */
    public static List<String> lrange(String  key, long start, long end) {
        return cacheUtils.redisTemplate.opsForList().range(prefix + key, start, end);
    }

    /**
     * 移除并且返回 prefix + key 对应的 list 的第一个元素
     *
     * @param prefix + key
     * @return
     */
    public static String lpop(String  key) {
        return cacheUtils.redisTemplate.opsForList().leftPop(prefix + key);
    }

    /**
     * 保存到hash集合中 只在 prefix + key 指定的哈希集中不存在指定的字段时，设置字段的值。如果 prefix + key 指定的哈希集不存在，会创建一个新的哈希集并与
     * prefix + key 关联。如果字段已存在，该操作无效果。
     *
     * @param hName
     *            集合名
     * @param prefix + key
     * @param val
     */
    public static void hsetnx(String hName, String  key, String value) {
        cacheUtils.redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.hSetNX((prefix + key).getBytes(),
                (prefix + key).getBytes(), value.getBytes()));

    }

    /**
     * 保存到hash集合中 只在 prefix + key 指定的哈希集中不存在指定的字段时，设置字段的值。如果 prefix + key 指定的哈希集不存在，会创建一个新的哈希集并与
     * prefix + key 关联。如果字段已存在，该操作无效果。
     *
     * @param <T>
     *
     * @param hName
     *            集合名
     * @param prefix + key
     * @param val
     */
    public static <T> void hsetnx(String hName, String  key, T t) {
        hsetnx(hName, prefix + key, JacksonUtils.obj2json(t));
    }
}
