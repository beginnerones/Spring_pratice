package hello.firebase.controller;

import hello.firebase.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class RedisController {

    private final RedisService redisService;

    @Autowired
    public RedisController(RedisService redisService) {
        this.redisService=redisService;
    }

    @PostMapping("/redis/save")
    public ResponseEntity<?> SaveData(@RequestParam String key,@RequestParam String value){
        redisService.saveData(key,value);
        return ResponseEntity.ok().body(Map.of("key",key,"value",value));
    }

    @GetMapping("/redis/search")
    public ResponseEntity<?> SearchData(@RequestParam String key){
        Object n=redisService.getData(key);
        return ResponseEntity.ok().body(n);
    }

}
