package run.halo.app.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import run.halo.app.model.dto.ReadTrendDto;
import run.halo.app.model.support.HaloConst;
import run.halo.app.service.PostService;
import run.halo.app.utils.DateUtils;

import javax.annotation.PostConstruct;

/**
 * @ClassName RecordReadTimesTrendTask
 * @Description record reading times to cache.
 * if you want to use this function, you must set halo.cache=redis in application-user.yaml,
 * and config your own redis host/hosts and password.
 * @Author huangmingren
 * @Date 2020/11/8 12:55
 **/
public class RecordReadTimesTrendTask {

    private static final Logger log = LoggerFactory.getLogger(RecordReadTimesTrendTask.class);

    private Integer yesterdayTotalReadTimes = 0;

    private Integer lastMonthTotalReadTimes = 0;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    PostService postService;

    @PostConstruct
    public void init(){
        Long countVisit = postService.countVisit();
        this.yesterdayTotalReadTimes = countVisit.intValue();
        this.lastMonthTotalReadTimes = countVisit.intValue();
    }

    /**
     * @Author huangmingren
     * @Description record a week or a month reading times to redis, the base unit is a day
     * @Date 2020/11/8 14:59
     * @Param []
     * @Return void
     **/
    @Scheduled(cron = "5 5 1 * * ?")
    public synchronized void everydayReadTimesTrend(){
        int i = incrementTotal(this.yesterdayTotalReadTimes, 0);
        ReadTrendDto readTrendDto = getReadTrendDto(i);
        redisTemplate.opsForList().leftPush(HaloConst.EVERY_DAY_READ_TIMES_REDIS_KEY, readTrendDto);
        Integer size = redisTemplate.opsForList().size(HaloConst.EVERY_DAY_READ_TIMES_REDIS_KEY).intValue();
        while (size > 30){
            redisTemplate.opsForList().rightPop(HaloConst.EVERY_DAY_READ_TIMES_REDIS_KEY);
            size--;
        }
    }

    /**
     * @Author huangmingren
     * @Description record three month reading times to redis, the base unit is week total read times, first day of the month will exec this function
     * @Date 2020/11/8 15:22
     * @Param []
     * @Return void
     **/
    @Scheduled(cron = "0 0 0 1 * ?")
    public synchronized void yearReadTimesTrend(){
        int i = incrementTotal(this.lastMonthTotalReadTimes, 1);
        ReadTrendDto readTrendDto = getReadTrendDto(i);
        redisTemplate.opsForList().leftPush(HaloConst.EVERY_WEEK_READ_TIMES_REDIS_KEY, readTrendDto);
        Integer size = redisTemplate.opsForList().size(HaloConst.EVERY_WEEK_READ_TIMES_REDIS_KEY).intValue();
        while (size > 12){
            redisTemplate.opsForList().rightPop(HaloConst.EVERY_WEEK_READ_TIMES_REDIS_KEY);
            size--;
        }
    }

    private ReadTrendDto getReadTrendDto(int times){
        ReadTrendDto readTrendDto = new ReadTrendDto();
        readTrendDto.setTimes(times);
        readTrendDto.setDate(DateUtils.now().getTime());
        return readTrendDto;
    }

    private int incrementTotal(Integer before, int i){
        Long countVisit = postService.countVisit();
        int value = countVisit.intValue();
        if(i == 0){
            this.yesterdayTotalReadTimes = value;
        }else{
            this.lastMonthTotalReadTimes = value;
        }
        return value - before;
    }
}
