package run.halo.app.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ClassName ReadTrendDto
 * @Description reading trend message
 * @Author huangmingren
 * @Date 2020/11/8 11:51
 **/
@Getter
@Setter
@NoArgsConstructor
public class ReadTrendDto implements Serializable {

    private static final long serialVersionUID = 149684127803583894L;
    private Integer times;

    private Long date;

    @Override
    public String toString() {
        return "ReadTrendDto{" +
                "times=" + times +
                ", date='" + date + '\'' +
                '}';
    }
}
