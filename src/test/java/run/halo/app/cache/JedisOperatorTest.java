package run.halo.app.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import run.halo.app.Application;
import run.halo.app.config.properties.HaloProperties;
import run.halo.app.utils.DateUtils;
import run.halo.app.utils.JsonUtils;

/**
 * @ClassName JedisOperatorTest
 * @Description
 * @Author huangmingren
 * @Date 2020/11/8 20:07
 **/
@SpringBootTest(classes = Application.class)
@ActiveProfiles(value = "user")
public class JedisOperatorTest {

    @Autowired
    private HaloProperties haloProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testJedisPool() throws JsonProcessingException {
        CacheWrapper<Integer> original = new CacheWrapper<>();
        original.setExpireAt(DateUtils.now());
        original.setCreateAt(DateUtils.now());
        original.setData(1);
        ObjectMapper objectMapper = new ObjectMapper();
//        String s = objectMapper.writeValueAsString(original);
        String s = JsonUtils.objectToJson(original);
        redisTemplate.opsForValue().set("test", s);
    }


}
