package hello.firebase.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.firebase.exception.NotExistException;
import hello.firebase.exception.TokenException;
import hello.firebase.exception.resolver.InvalidException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }catch(ExpiredJwtException | IllegalArgumentException e){
            setErrorResponse(response,ErrorCode.TOKEN_EXPIRED,e.getMessage());
        }catch(JwtException e){
            setErrorResponse(response,ErrorCode.INVALID_TOKEN,e.getMessage());
        }catch(NotExistException e){
            setErrorResponse(response,ErrorCode.NOT_EXIST_TOKEN,e.getMessage());
        }catch(Exception e){
            setErrorResponse(response,ErrorCode.EXCEPTION,e.getMessage());
        }

    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode,String message) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getCode());
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage(),message);
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Data
    public static class ErrorResponse{
        private final Integer status;
        private final String message;
        private final String error;
    }
}
