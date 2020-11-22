package run.halo.app.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import run.halo.app.model.dto.ReadTrendDto;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * InMemoryCacheStoreTest.
 *
 * @author johnniang
 * @date 3/28/19
 */
class InMemoryCacheStoreTest {

    InMemoryCacheStore cacheStore;

    @BeforeEach
    void setUp() {
        cacheStore = new InMemoryCacheStore();
    }

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

        // Get the caceh
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
    public void JsonArray(){
        ArrayList<ReadTrendDto> readTrendDtos = new ArrayList<>();
        ReadTrendDto readTrendDto1 = new ReadTrendDto();
        readTrendDto1.setTimes(2);
        readTrendDto1.setDate(System.currentTimeMillis());
        readTrendDtos.add(readTrendDto1);
        readTrendDtos.add(readTrendDto1);
        readTrendDtos.add(readTrendDto1);
        readTrendDtos.add(readTrendDto1);
        readTrendDtos.add(readTrendDto1);
        readTrendDtos.add(readTrendDto1);
        readTrendDtos.add(readTrendDto1);
        readTrendDtos.add(readTrendDto1);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int size = readTrendDtos.size();

        int index = 0;
        System.out.println(size + "-----" + index);
        for (ReadTrendDto readTrendDto : readTrendDtos){
            sb.append("[").append(readTrendDto.getTimes()).append(",").append(readTrendDto.getTimes()).append("]");
            if(index++ == size - 1) continue;
            sb.append(",");
//            index++;
        }
        sb.append("]");
        System.out.println(sb.toString());
    }
}
