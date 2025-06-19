package hello.firebase.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

//토큰 관련 기능들 모아두기.
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class JwtUtil {

    //토큰 유효시간 지났는지,유효한지 확인
    public TokenStatus getTokenStatus(String token, Key secretKey) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return TokenStatus.AUTHENTICATED;
            //토큰 상태 체크
        }catch(ExpiredJwtException | IllegalArgumentException e){
            throw new IllegalArgumentException("유효기간 지남."); //유효기간 지남.
        }
        catch(JwtException e){
            throw new JwtException("토큰 검증 오류");
        }

    }

    //쿠키에서 jwt토큰 정보만 받아오기.
    public String resolveTokenFromCookie(Cookie[] cookies, JwtRule tokenPrefix){
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(tokenPrefix.getValue()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");
    }

    //특정 토큰의 시크릿 키로 사용할때,비밀키 생성.
    public Key getSigningKey(String secretKey){
        String encodedKey=encodeToBase64(secretKey);
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //문자열 인코딩.
    private String encodeToBase64(String secretKey){
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public Cookie resetToken(JwtRule tokenPrefix){
        Cookie cookie= new Cookie(tokenPrefix.getValue(),null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }





}
