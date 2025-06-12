package hello.firebase.filter;

import hello.firebase.config.RateLimiterConfig;
import hello.firebase.domain.User;
import hello.firebase.exception.NotExistException;
import hello.firebase.other.JwtUtil;
import hello.firebase.other.TokenStatus;
import hello.firebase.service.JwtService;
import hello.firebase.service.TestService;
import io.github.bucket4j.Bucket;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@Order(2)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final TestService testService;
    private final ConcurrentHashMap<String, Bucket> rateLimiters;
    private final RateLimiterConfig rateLimiter;

    private static final List<AntPathRequestMatcher> saveMat=List.of(new AntPathRequestMatcher("/user/save","POST"),
            new AntPathRequestMatcher("/user/reissue","POST"));


    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, JwtService jwtService, TestService testService, ConcurrentHashMap<String, Bucket> rateLimiters, RateLimiterConfig rateLimiter) {
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
        this.testService = testService;
        this.rateLimiters = rateLimiters;
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return saveMat.stream().anyMatch(m -> m.matches(request)); //여기 매칭되면 작동안함.
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            log.info("authHeader:{}",authHeader);
            if (authHeader != null && authHeader.startsWith(("Bearer "))) {
                String token = authHeader.substring(7); //토큰 정보 빼오기

                TokenStatus status = jwtService.checkAccessToken(token); //토큰 상태확인.여기서 예외던짐.
                log.info("status:{}",status);
                if (status == TokenStatus.AUTHENTICATED) { //유효시
                    Claims claims = jwtService.getClaims(token);
                    String userId = claims.getSubject();
                    log.info("userId:{}",userId);
                    setAuthenticationContext(userId);
                    filterChain.doFilter(request, response);
                    return;
                } else { //유효하지 않을시.
                    throw new JwtException("Invalid token");
                }

            }
            //restapi
//            String refreshToken = request.getHeader("Refresh");
//            if (refreshToken != null && refreshToken.startsWith(("Bearer "))) {
//                String token = refreshToken.substring(7);
//                try {
//                    User user = findByIdentifier(refreshToken);
//                    if (jwtService.validateRefreshToken(refreshToken, user.getEmail())) {  //검증.
//                        String reAccessToken = jwtService.generateAccessToken(user);
//                    }
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
        } catch (Exception e) {
            throw e;
        }
        filterChain.doFilter(request, response);
    }

    private User findByIdentifier(String refreshToken) throws Exception {
        String identifier = jwtService.getIdentifierFromRefresh(refreshToken);
        return testService.findByIdentifier(identifier);

    }

    private void setAuthenticationContext(String AccessToken){
       UsernamePasswordAuthenticationToken authentication=jwtService.getAuthentication(AccessToken);
       SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
