package run.halo.app.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName ReadTimesTrendVO
 * @Description
 * @Author huangmingren
 * @Date 2020/11/20 20:47
 **/
@Data
public class ReadTimesTrendVO {
    private Integer max;

    private List<String> x;

    private List<Integer> y;

    private Integer interval;

    private String unit;




}
