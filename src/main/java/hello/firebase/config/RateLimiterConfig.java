package hello.firebase.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class RateLimiterConfig {

    private static final int CAPACITY =20; //버킷에 담기는 토큰 최대수.

    @Bean
    public Bucket bucket() {
        return Bucket.builder()
                .addLimit(limit-> limit.capacity(10).refillGreedy(10, Duration.ofMinutes(1)))
                .build(); //최대 용량 50,1분마다 50개 지금.

    }
}
