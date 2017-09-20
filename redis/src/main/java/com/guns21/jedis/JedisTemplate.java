package com.guns21.jedis;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mazw on 2016/3/17.
 */
public class JedisTemplate {
    private static Logger logger = LoggerFactory.getLogger(JedisTemplate.class);
    private BeanSerializer serializer;
    private GenericObjectPoolConfig jedisPoolConfig;
    private String hostName;
    private int port;
    private String password;
    private JedisPool jedisPool;

    public JedisTemplate(GenericObjectPoolConfig jedisPoolConfig, String hostName, int port, BeanSerializer serializer) {
        this.jedisPoolConfig = jedisPoolConfig;
        this.hostName = hostName;
        this.port = port;
        this.jedisPool = new JedisPool(jedisPoolConfig, hostName, port);
        this.serializer = serializer;
    }

    public JedisTemplate(GenericObjectPoolConfig jedisPoolConfig, String hostName, int port, String password, BeanSerializer serializer) {
        this.jedisPoolConfig = jedisPoolConfig;
        this.hostName = hostName;
        this.port = port;
        this.jedisPool = new JedisPool(jedisPoolConfig, hostName, port, 2000, password);
        this.serializer = serializer;
    }

    public BeanSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(BeanSerializer serializer) {
        this.serializer = serializer;
    }

    public GenericObjectPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(GenericObjectPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    private byte[] sk(String key) {
        return serializer.serialKey(key);
    }

    private byte[] sv(Object value) {
        return serializer.serialValue(value);
    }

    private String dk(byte[] keyb) {
        return serializer.deserializeKey(keyb);
    }

    private Object dv(byte[] value) {
        return serializer.deserializeValue(value);
    }

    /* ======================= */

    public Boolean set(String key, Object value) {
        if (StringUtils.isEmpty(key) || ObjectUtils.isEmpty(value)) {
            return false;
        }
        String result = "";
        try (Jedis jedis = getJedis()) {
            result = jedis.set(sk(key), sv(value));
        } catch (Exception e) {
            logger.error("JedisTemplate.get()异常", e);
        }
        return "ok".equalsIgnoreCase(result);
    }

    /**
     * 查询缓存信息
     */

    public <T> T get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        byte[] value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.get(sk(key));
        } catch (Exception e) {
            logger.error("JedisTemplate.get()异常", e);
        }

        if (ArrayUtils.isEmpty(value)) {
            return null;
        }
        return (T) dv(value);
    }


    public List<String> lrange(String key, int from, int end) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        List<String> lrange = null;
        try (Jedis jedis = getJedis()) {
            lrange = jedis.lrange(key, from, end);
        } catch (Exception e) {
            logger.error("JedisTemplate.lrange()异常", e);
        }
        return lrange;
    }


    public Boolean setOnSeconds(String key, Object value, int seconds) {
        if (ObjectUtils.isEmpty(key) || ObjectUtils.isEmpty(value)) {
            return false;
        }
        try (Jedis jedis = getJedis()) {
            String result = jedis.set(sk(key), sv(value));

            if ("ok".equalsIgnoreCase(result)) {
                if (seconds > 0) {
                    jedis.expire(sk(key), seconds);
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("JedisTemplate.setOnSeconds()异常", e);
        }
        return true;
    }

    /**
     * 删除缓存
     */

    public Boolean delete(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        Long result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.del(sk(key));
        } catch (Exception e) {
            logger.error("JedisTemplate.delete()异常", e);
        }
        return result > 0;
    }

    /**
     * 设置过期时间
     */

    public Boolean expire(String key, int seconds) {
        if (seconds <= 0) {
            return false;
        }

        Long result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.expire(sk(key), seconds);
        } catch (Exception e) {
            logger.error("JedisTemplate.expire()异常", e);
        }
        return result > 0;
    }

    /**
     * 查询过期时间
     */

    public long ttl(String key) {
        if (StringUtils.isEmpty(key)) {
            return -1;
        }

        Long result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.ttl(sk(key));
        } catch (Exception e) {
            logger.error("JedisTemplate.ttl()异常", e);
        }
        return result;
    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在。
     * 设置成功，返回 true 。
     * 设置失败，返回 false 。
     */

    public Boolean setnx(String key, String value) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }

        boolean isSuccess = true;
        try (Jedis jedis = getJedis()) {
            Long result = jedis.setnx(sk(key), sv(value));
            isSuccess = result == 1;
            jedis.expire(key, 3); //3秒钟后过期
        } catch (Exception e) {
            logger.error("JedisTemplate.setnx()异常", e);
        }
        return isSuccess;
    }


    public Boolean exists(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }

        Boolean result = true;
        try (Jedis jedis = getJedis()) {
            result = jedis.exists(sk(key));
        } catch (Exception e) {
            logger.error("JedisTemplate.exists()异常", e);
        }
        return result;
    }


    public Boolean hasKeyLike(String patt) {
        if (StringUtils.isEmpty(patt)) {
            return Boolean.FALSE;
        }

        Set<String> keys = null;
        try (Jedis jedis = getJedis()) {
            keys = jedis.keys("*" + patt + "*");
        } catch (Exception e) {
            logger.error("JedisTemplate.hasKeyLike()异常", e);
        }

        return keys != null && keys.size() > 0;
    }


    public Long rpush(String key, String... value) {
        if (StringUtils.isBlank(key)) {
            return -1l;
        }
        if (ArrayUtils.isEmpty(value)) {
            return -1l;
        }


        long count = 0l;
        try (Jedis jedis = getJedis()) {
            count = jedis.rpush(sk(key), sv(value));
        } catch (Exception e) {
            logger.error("JedisTemplate.rpush()异常", e);
        }
        return count;
    }


    public Long llen(String key) {
        if (StringUtils.isBlank(key)) {
            return -1l;
        }


        long count = 0l;
        try (Jedis jedis = getJedis()) {
            count = jedis.llen(sk(key));
        } catch (Exception e) {
            logger.error("JedisTemplate.llen()异常", e);
        }
        return count;
    }


    public List<String> lrange(String key, Long from, Long to) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        List<String> list = null;
        try (Jedis jedis = getJedis()) {
            jedis.multi();
            list = jedis.lrange(key, from, to);
        } catch (Exception e) {
            logger.error("JedisTemplate.lrange()异常", e);
        }
        return list;
    }


    public Object lpop(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }


        byte[] value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.lpop(sk(key));
        } catch (Exception e) {
            logger.error("JedisTemplate.lpop()异常", e);
        }
        if (ObjectUtils.isEmpty(value) || "nil".equalsIgnoreCase(Arrays.toString(value))) {
            return null;
        }
        return dv(value);
    }


    public Object rpop(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }


        byte[] value = null;
        try (Jedis jedis = getJedis()) {
            value = jedis.rpop(sk(key));
        } catch (Exception e) {
            logger.error("JedisTemplate.lpop()异常", e);
        }
        if (ObjectUtils.isEmpty(value) || "nil".equalsIgnoreCase(Arrays.toString(value))) {
            return null;
        }
        return dv(value);
    }


    public Long increment(String key, int delta) {
        if (StringUtils.isBlank(key)) {
            return null;
        }


        Long number = null;
        try (Jedis jedis = getJedis()) {
            number = jedis.incrBy(sk(key), delta);
        } catch (Exception e) {
            logger.error("JedisTemplate.increment()异常", e);
        }

        return number;
    }


    public Long lPushAll(String key, String[] luckNumArr) {
        if (StringUtils.isBlank(key)) {
            return null;
        }


        Long size = null;
        try (Jedis jedis = getJedis()) {
            size = jedis.lpush(key, luckNumArr);
        } catch (Exception e) {
            logger.error("JedisTemplate.lPushAll()异常", e);
        }

        return size;
    }


    public Long hset(String key, String field, Object value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field) || ObjectUtils.isEmpty(value)) {
            return -1l;
        }

        Long result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.hset(sk(key), sk(field), sv(value));
        } catch (Exception e) {
            logger.error("JedisTemplate.hset()异常", e);
        }
        return result;
    }

    public Boolean hmset(String key, Map<String, Object> map) {
        if (StringUtils.isEmpty(key) || ObjectUtils.isEmpty(map)) {
            return false;
        }

        String result = null;
        Map<byte[], byte[]> value = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            value.put(sk(entry.getKey()), sv(entry.getValue()));
        }

        try (Jedis jedis = getJedis()) {
            result = jedis.hmset(sk(key), value);
        } catch (Exception e) {
            logger.error("JedisTemplate.hmset()异常", e);
        }
        return "ok".equalsIgnoreCase(result);
    }

    public Long hdel(String key, String field) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            return -1l;
        }

        Long result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.hdel(sk(key), sk(field));
        } catch (Exception e) {
            logger.error("JedisTemplate.hdel()异常", e);
        }
        return result;
    }

    public Object hget(String key, String field) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            return -1l;
        }

        byte[] result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.hget(sk(key), sk(field));
        } catch (Exception e) {
            logger.error("JedisTemplate.hget()异常", e);
        }
        if (ObjectUtils.isEmpty(result) || "nil".equalsIgnoreCase(Arrays.toString(result))) {
            return null;
        }
        return dv(result);
    }

    public Map<String, Object> hgetAll(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        Map<byte[], byte[]> result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.hgetAll(sk(key));
        } catch (Exception e) {
            logger.error("JedisTemplate.hgetAll()异常", e);
        }
        if (ObjectUtils.isEmpty(result)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<byte[], byte[]> entry : result.entrySet()) {
            map.put(dk(entry.getKey()), dv(entry.getValue()));
        }
        return map;
    }

    public Long hincrby(String key, String field, long num) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            return -1l;
        }

        Long re = null;
        try (Jedis jedis = getJedis()) {
            re = jedis.hincrBy(sk(key), sk(field), num);
        } catch (Exception e) {
            logger.error("JedisTemplate.hincrby()异常", e);
        }
        return re;
    }


    public Boolean hexists(String key, String field) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            return false;
        }
        Boolean result = true;
        try (Jedis jedis = getJedis()) {
            result = jedis.hexists(sk(key), sk(field));
        } catch (Exception e) {
            logger.error("JedisTemplate.hexists()异常", e);
        }
        return result;
    }


    public Long lpush(String key, Object value) {
        if (StringUtils.isBlank(key) || ObjectUtils.isEmpty(value)) {
            return null;
        }

        Long result = null;
        try (Jedis jedis = getJedis()) {
            result = jedis.lpush(sk(key), sv(value));
        } catch (Exception e) {
            logger.error("JedisTemplate.lpush()异常", e);
        }
        return result;
    }


    public Map<String, Object> hgetAllAndDel(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Jedis jedis = getJedis();
        Transaction multi = jedis.multi();
        Response<Map<byte[], byte[]>> result = null;
        try {
            result = multi.hgetAll(sk(key));
            multi.del(sk(key));
        } catch (Exception e) {
            multi.discard();
            logger.error("JedisTemplate.hgetAllAndDel()异常", e);
        } finally {
            multi.exec();
            if (jedis != null) {
                jedis.close();
            }
        }
        if (ObjectUtils.isEmpty(result)) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<byte[], byte[]> entry : result.get().entrySet()) {
            map.put(dk(entry.getKey()), dv(entry.getValue()));
        }
        return map;
    }
}
