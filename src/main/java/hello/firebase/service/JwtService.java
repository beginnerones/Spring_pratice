package hello.firebase.service;

import hello.firebase.domain.User;
import hello.firebase.dto.TokenDto;
import hello.firebase.util.JwtGenerator;
import hello.firebase.util.JwtUtil;
import hello.firebase.util.TokenStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;

@Service
@Transactional
@Slf4j
public class JwtService {
    private final CustomUserdetailsService custom;
    private final JwtGenerator jwtGenerator;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final TestService testService;

    private final Key ACCESS_SECRET_KEY;
    private final Key REFRESH_SECRET_KEY;
    private final long ACCESS_EXPIRATION;
    private final long REFRESH_EXPIRATION;
    private final ServletContext servletContext;

    public JwtService(CustomUserdetailsService custom,
                      JwtGenerator jwtGenerator, JwtUtil jwtUtil, RedisService redisService,
                      TestService testService,
                      @Value("${jwt.access-secret}")String ACCESS_SECRET_KEY,
                      @Value("${jwt.refresh-secret}") String REFRESH_SECRET_KEY,
                      @Value("${jwt.access-expiration}") long ACCESS_EXPIRATION,
                      @Value("${jwt.refresh-expiration}") long REFRESH_EXPIRATION, ServletContext servletContext) {
        this.custom = custom;
        this.jwtGenerator = jwtGenerator;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
        this.testService = testService;
        this.ACCESS_SECRET_KEY = jwtUtil.getSigningKey(ACCESS_SECRET_KEY);
        this.REFRESH_SECRET_KEY = jwtUtil.getSigningKey(REFRESH_SECRET_KEY);
        this.ACCESS_EXPIRATION = ACCESS_EXPIRATION;
        this.REFRESH_EXPIRATION = REFRESH_EXPIRATION;
        this.servletContext = servletContext;
    }

    public String generateAccessToken(User requestUser){
        String accessToken=jwtGenerator.generateAccessToken(ACCESS_SECRET_KEY,ACCESS_EXPIRATION,requestUser);
        return accessToken;
    }

    private void getSigningKey(Key accessSecretKey) {

    }

    public String generateRefreshToken(User requestUser){
        String refreshToken=jwtGenerator.generateRefreshToken(REFRESH_SECRET_KEY,REFRESH_EXPIRATION,requestUser);
        redisService.saveData(refreshToken,requestUser.getEmail());
        return refreshToken;
    }

    //토큰 상태 확인.
    public boolean validateAccessToken(String token){
        return jwtUtil.getTokenStatus(token,ACCESS_SECRET_KEY) == TokenStatus.AUTHENTICATED;
    }

    public TokenStatus checkAccessToken(String token){
        return jwtUtil.getTokenStatus(token,ACCESS_SECRET_KEY);
    }

    public TokenStatus checkRefreshToken(String token){
        return jwtUtil.getTokenStatus(token,REFRESH_SECRET_KEY);
    }

    //토큰과 개인 인식 번호 맞는지,여기서
    public boolean validateRefreshToken(String token,String identifier){
        boolean isRefreshValid=jwtUtil.getTokenStatus(token,REFRESH_SECRET_KEY) == TokenStatus.AUTHENTICATED;
        boolean isTokenMatched=false;
        Object tokens=redisService.getData(token);
        if(tokens!=null){
           isTokenMatched =true;
        }else throw new RuntimeException("토큰이 존재하지 않습니다.");
        return isRefreshValid&&isTokenMatched;
    }

    //인가되게 바꾸기.
//    public Authentication getAuthentication(String token){
//        UserDetails principal= custom.loadUserByUsername(getUserPk(token,ACCESS_SECRET_KEY));
//        return new UsernamePasswordAuthenticationToken(principal,"",principal.getAuthorities());
//    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(ACCESS_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String getUserPk(String token, Key accessSecretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(accessSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getIdentifierFromRefresh(String refreshToken)throws Exception {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(REFRESH_SECRET_KEY)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public TokenDto regenerateToken(String refreshToken){
        TokenDto tokenDto=new TokenDto();
        if (refreshToken != null && refreshToken.startsWith(("Bearer "))) {
            String token = refreshToken.substring(7);
            log.info(token);
            try {
                User user = findByIdentifier(token);
                if (validateRefreshToken(token, user.getEmail())) {  //검증.
                    tokenDto.setRefreshToken(generateRefreshToken(user));
                    tokenDto.setAccessToken(generateAccessToken(user));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new RuntimeException("refreshToken이 존재하지 않습니다.");
        }

        return tokenDto;
    }

    private User findByIdentifier(String refreshToken) throws Exception {
        String identifier = getIdentifierFromRefresh(refreshToken); //iden 빼내기
        return testService.findByIdentifier(identifier);

    }

    public UsernamePasswordAuthenticationToken getAuthentication(String userName){  //인가 설정 메소드.
        UserDetails principal = custom.loadUserByUsername(userName);
        return new UsernamePasswordAuthenticationToken(principal,"",principal.getAuthorities());
    }






}
