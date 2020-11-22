package run.halo.app.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import run.halo.app.config.properties.HaloProperties;
import run.halo.app.utils.DateUtils;
import run.halo.app.utils.JsonUtils;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

/**
 * @ClassName RedisCacheStore
 * @Description not a cluster redis cache store
 * @Author huangmingren
 * @Date 2020/11/8 16:41
 **/
public class RedisCacheStore extends AbstractStringCacheStore{

    private static final Logger log = LoggerFactory.getLogger(RedisCacheStore.class);

    @Autowired
    private RedisTemplate redisTemplate;


    public RedisCacheStore(HaloProperties haloProperties){
        this.haloProperties = haloProperties;
    }

    @Override
    Optional<CacheWrapper<String>> getInternal(String key) {
        Assert.hasText(key, "Cache key must not be blank");
        Object v = redisTemplate.opsForValue().get(key);
        return StringUtils.isEmpty(v) ? Optional.empty() : jsonToCacheWrapper(String.valueOf(v));
    }

    @Override
    void putInternal(String key, CacheWrapper<String> cacheWrapper) {
        putInternalIfAbsent(key, cacheWrapper);
//        try {
//            Date expireAt = cacheWrapper.getExpireAt();
//            Date createAt = cacheWrapper.getCreateAt();
//            redisTemplate.opsForValue().set(key, cacheWrapper.getData(), DateUtils.subduction(expireAt, createAt), TimeUnit.MILLISECONDS);
//        } catch (Exception e) {
//            log.error("Put cache fail json2object key: [{}] value:[{}]", key, cacheWrapper);
//            e.printStackTrace();
//        }
    }

    @Override
    Boolean putInternalIfAbsent(String key, CacheWrapper<String> cacheWrapper) {
        Assert.hasText(key, "Cache key must not be blank");
        Assert.notNull(cacheWrapper, "Cache wrapper must not be null");
        Date createAt = cacheWrapper.getCreateAt();
        Date ttl = cacheWrapper.getExpireAt();
        try {
            redisTemplate.opsForValue().set(key, JsonUtils.objectToJson(cacheWrapper));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (ttl != null && createAt != null) {
            redisTemplate.expire(key, Duration.ofMillis(DateUtils.subduction(createAt, ttl)));
        }
        return true;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
