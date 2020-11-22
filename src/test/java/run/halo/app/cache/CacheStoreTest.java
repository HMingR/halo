package run.halo.app.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import run.halo.app.utils.DateUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CacheStoreTest.
 *
 * @author johnniang
 * @date 3/28/19
 */
class CacheStoreTest {

    AbstractStringCacheStore cacheStore = new InMemoryCacheStore();

    @Test
    void putNullValueTest() {
        String key = "test_key";

        assertThrows(IllegalArgumentException.class, () -> cacheStore.put(key, null));
    }

    @Test
    void putNullKeyTest() {
        String value = "test_value";

        assertThrows(IllegalArgumentException.class, () -> cacheStore.put(null, value));
    }

    @Test
    void getByNullKeyTest() {
        assertThrows(IllegalArgumentException.class, () -> cacheStore.get(null));
    }


    @Test
    void getNullTest() {
        String key = "test_key";

        Optional<String> valueOptional = cacheStore.get(key);

        assertFalse(valueOptional.isPresent());
    }

    @Test
    void expirationTest() throws InterruptedException {
        String key = "test_key";
        String value = "test_value";
        cacheStore.put(key, value, 500, TimeUnit.MILLISECONDS);

        Optional<String> valueOptional = cacheStore.get(key);

        assertTrue(valueOptional.isPresent());
        assertEquals(value, valueOptional.get());

        TimeUnit.SECONDS.sleep(1L);

        valueOptional = cacheStore.get(key);

        assertFalse(valueOptional.isPresent());
    }

    @Test
    void deleteTest() {
        String key = "test_key";
        String value = "test_value";

        // Put the cache
        cacheStore.put(key, value);

        // Get the cache
        Optional<String> valueOptional = cacheStore.get(key);

        // Assert
        assertTrue(valueOptional.isPresent());
        assertEquals(value, valueOptional.get());

        // Delete the cache
        cacheStore.delete(key);

        // Get the cache again
        valueOptional = cacheStore.get(key);

        // Assertion
        assertFalse(valueOptional.isPresent());
    }

    @Test
    public void testJson2Object() throws JsonProcessingException {
        CacheWrapper<Integer> original = new CacheWrapper<>();
        original.setExpireAt(DateUtils.now());
        original.setCreateAt(DateUtils.now());
        original.setData(1);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(original);
        System.out.println(s);
        CacheWrapper cacheWrapper = objectMapper.readValue(s, CacheWrapper.class);
        System.out.println(cacheWrapper);
    }

    @Test
    public void test(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1000);
        integers.add(7000);
        integers.add(5000);
        integers.add(4000);
        integers.add(13000);
        for (Integer item : integers){
            item = item / 1000;
        }
        System.out.println(integers);
    }
}
