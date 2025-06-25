package hello.firebase.controller;

import hello.firebase.dao.TestDto;
import hello.firebase.domain.User;
import hello.firebase.dto.TokenDto;
import hello.firebase.dto.UserDto;
import hello.firebase.service.JwtService;
import hello.firebase.service.TestService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@Slf4j
public class TestController {

    public TestService testService;
    public final JwtService jwtService;
    @Value("${server.env}")
    private String env;

    @Autowired
    public TestController(TestService testService, JwtService jwtService) {
        this.testService = testService;
        this.jwtService = jwtService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        log.info("test");
        return ResponseEntity.ok("test");
    }

    @PostMapping("/user/save")
    public ResponseEntity<?> saveUser(@RequestBody UserDto userDto, HttpServletResponse response)throws Exception {

        User user = testService.saveUser(userDto);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        response.setHeader("Authorization", "Bearer "+accessToken);
        response.setHeader("Refresh", "Bearer "+refreshToken);

        return ResponseEntity.ok().body(Map.of(
                "accesToken",accessToken,
                "refreshToken",refreshToken
        ));
    }

    @PostMapping("/user/reissue")
    public ResponseEntity<?> reissueUser(@RequestHeader("Refresh") String refreshToken){
        log.info("reissueUser");
        TokenDto tokenDto=jwtService.regenerateToken(refreshToken);
        return ResponseEntity.ok().body(tokenDto);
    }

    @GetMapping("/env")
    public ResponseEntity<?> getEnv(){
        return ResponseEntity.ok(env);
    }

}
