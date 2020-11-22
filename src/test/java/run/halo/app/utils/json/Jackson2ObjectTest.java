package run.halo.app.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import run.halo.app.model.dto.ReadTrendDto;

import java.util.List;

/**
 * @ClassName Jackson2ObjectTest
 * @Description TODO
 * @Author huangmingren
 * @Date 2020/11/8 12:19
 **/
public class Jackson2ObjectTest {

    public static void main(String[] args) throws JsonProcessingException {
        String s = "{\"domain_list\":[{\"times\":8,\"date\":\"2020.10.19\"},{\"times\":11,\"date\":\"2020.10.19\"},{\"times\":12,\"date\":\"2020.10.19\"}]}";
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectReader objectReader = objectMapper.readerForListOf(ReadTrendDto.class);
        List<ReadTrendDto> o = objectReader.readValue(s);
        System.out.println(o.size());

    }

}
