package hello.firebase.util;

import hello.firebase.domain.User;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtGenerator {  //헤더,claims에 들어갈 정보 받아 jwt를 빌드에 반환한다.
    public String generateAccessToken(final Key ACCESS_SECRET, final long ACCESS_EXPIRATION, User user){
        Long now=System.currentTimeMillis(); //발급 시간.

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(createClaims(user))
                .setSubject(String.valueOf(user.getUser()))
                .setExpiration(new Date(now+ACCESS_EXPIRATION))
                .signWith(ACCESS_SECRET, SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, ?> createClaims(User user) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("Identifier",user.getEmail());
        return claims;
    }

    private Map<String, Object> createHeader() { //헤더 생성.
        Map<String,Object>header=new HashMap<>();
        header.put("typ","JWT");
        header.put("alg","HS256");
        return header;
    }

    //그냥 access 재발급 용도이기 때문에 claims 에 아무것도 넣지 않음

    public String generateRefreshToken(final Key REFRESH_SECRET, final long REFRESH_EXPIRATION, User user){
        Long now=System.currentTimeMillis();

        return Jwts.builder()
                .setHeader(createHeader())
                .setSubject(String.valueOf(user.getEmail()))
                .setExpiration(new Date(now+REFRESH_EXPIRATION))
                .signWith(REFRESH_SECRET,SignatureAlgorithm.HS256)
                .compact();

    }


}
