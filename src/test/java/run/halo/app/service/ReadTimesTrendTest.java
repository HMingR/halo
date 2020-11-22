package run.halo.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import run.halo.app.model.dto.ReadTrendDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ReadTimesTrendTest
 * @Description TODO
 * @Author huangmingren
 * @Date 2020/11/9 21:36
 **/
//@SpringBootTest(classes = Application.class)
public class ReadTimesTrendTest {

    //@Autowired
    //AdminService adminService;

    @Test
    public void getTrendByType() throws JsonProcessingException {
//        String readTrendByType = adminService.getReadTrendByType(0);
//        String readTrendByType1 = adminService.getReadTrendByType(1);
//        String readTrendByType2 = adminService.getReadTrendByType(2);
//        String readTrendByType3 = adminService.getReadTrendByType(3);
//        System.out.println(readTrendByType);
//        System.out.println(readTrendByType1);
//        System.out.println(readTrendByType2);
//        System.out.println(readTrendByType3);

        ArrayList<ReadTrendDto> readTrendDtos = new ArrayList<>();


        long time = 1602209707000L;

        for (int i = 0; i < 100; i++){
            ReadTrendDto readTrendDto = new ReadTrendDto();
            readTrendDto.setTimes(i);
            readTrendDto.setDate(time + 86535030);
            readTrendDtos.add(readTrendDto);
        }

        String s = convertReadTrendDto2String(readTrendDtos);
        System.out.println(s);


    }


    private String convertReadTrendDto2String(List<ReadTrendDto> readTrendDtos){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int size = readTrendDtos.size();

        int index = 0;
        for (ReadTrendDto readTrendDto : readTrendDtos){
            sb.append("[").append(readTrendDto.getDate()).append(",").append(readTrendDto.getTimes()).append("]");
            if(index + 1  != size ) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
