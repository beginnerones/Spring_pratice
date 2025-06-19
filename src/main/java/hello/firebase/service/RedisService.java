package hello.firebase.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String,Object> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveData(String Key,Object value){
        redisTemplate.opsForValue().set(Key,value,60,TimeUnit.MINUTES);
    }

    public void saveDataAndTimeOut(String Key, Object value, long timeout, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(Key,value,timeout,TimeUnit.MINUTES); //시간설정.
    }

    public void deleteData(String Key){
        redisTemplate.delete(Key); //삭제
    }

    public Object getData(String Key){
        return redisTemplate.opsForValue().get(Key); //가져오기.
    }

}
