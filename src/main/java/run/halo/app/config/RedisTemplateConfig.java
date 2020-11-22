package run.halo.app.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import run.halo.app.config.properties.HaloProperties;

import java.time.Duration;
import java.util.List;

/**
 * @ClassName RedisTemplateConfig
 * @Description redisTemplate config
 * @Author huangmingren
 * @Date 2020/11/9 15:22
 **/
@ConditionalOnProperty(name = "halo.cache", havingValue = "redis")
@Configuration
public class RedisTemplateConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisTemplateConfig.class);

    @Autowired
    HaloProperties haloProperties;

    @Bean
    @Primary
    public RedisTemplate<String,Object> redisTemplate(){
        log.info("~~~~~~~~~~create redisTemplate~~~~~~~~~~");

        LettuceConnectionFactory lettuceConnectionFactory = initRedis();
        //创建客户端连接
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 设置 Key 的默认序列化机制
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private LettuceConnectionFactory initRedis(){
        List<String> cacheRedisNodes = haloProperties.getCacheRedisNodes();
        HostAndPort hostAndPort;
        if(cacheRedisNodes.isEmpty() || cacheRedisNodes.size() == 0){
            log.error("halo.cache-redis-nodes is empty, " +
                    "you must config you redis in application config file or set halo.cache not equals redis");
            log.warn("redis host and port is error, you should follow this format ----> host:port, and will use default redis config ---> 127.0.0.1:6379");
            hostAndPort = new HostAndPort("127.0.0.1", 6379);
        }else{
            String s = cacheRedisNodes.get(0);
            String[] split = s.split(":");
            if(split.length != 2){
                log.error("redis host and port is error, you should follow this format ----> host:port, and will use default redis config ---> 127.0.0.1:6379");
            }
            hostAndPort = new HostAndPort(split[0], Integer.parseInt(split[1]));
        }
        return createLettuceConnectionFactory(hostAndPort, haloProperties.getCacheRedisPassword());
    }

    private LettuceConnectionFactory createLettuceConnectionFactory(HostAndPort hostAndPort, String password){

        //redis配置
        RedisConfiguration redisConfiguration = new
                RedisStandaloneConfiguration(hostAndPort.getHost(), hostAndPort.getPort());
//        ((RedisStandaloneConfiguration) redisConfiguration).setDatabase(dbIndex);
        ((RedisStandaloneConfiguration) redisConfiguration).setPassword(password);

        //连接池配置
        GenericObjectPoolConfig genericObjectPoolConfig =
                new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(15);
        genericObjectPoolConfig.setMinIdle(10);
        genericObjectPoolConfig.setMaxTotal(30);
        genericObjectPoolConfig.setMaxWaitMillis(10_000);

        //redis客户端配置
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder
                builder =  LettucePoolingClientConfiguration.builder().
                commandTimeout(Duration.ofMillis(10_000));

//        builder.shutdownTimeout(Duration.ofMillis(shutdownTimeOut));
        builder.poolConfig(genericObjectPoolConfig);
        LettuceClientConfiguration lettuceClientConfiguration = builder.build();

        //根据配置和客户端配置创建连接
        LettuceConnectionFactory lettuceConnectionFactory = new
                LettuceConnectionFactory(redisConfiguration,lettuceClientConfiguration);
        lettuceConnectionFactory .afterPropertiesSet();

        return lettuceConnectionFactory;
    }

}
