package hello.firebase.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.firebase.exception.UserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper=new ObjectMapper();


    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try{
            if(ex instanceof UserException){
                log.info("UserException resolver to 400");
                String accept = request.getHeader("accept");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,ex.getMessage()); //상태코드 넣기.

                if("application/json".equals(accept)){
                    Map<String,Object> errorReuslt=new HashMap<>();
                    errorReuslt.put("ex",ex.getClass());
                    errorReuslt.put("message",ex.getMessage());

                    String result = objectMapper.writeValueAsString(errorReuslt);

                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(result);
                    return new ModelAndView();
                }else{
                    return new ModelAndView("error/500"); //에러 지정.
                }
            }

        }catch (IOException e){
            log.error("resolver ex",e);
        }
        return null;
    }
}

