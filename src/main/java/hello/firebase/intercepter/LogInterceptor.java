package hello.firebase.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
        //싱글톤이라서 uuid생성하면 안됨.
        //어트리뷰트로 넣음.
        request.setAttribute("uuid", uuid);
        if(handler instanceof HandlerMethod) {
            //핸들러는 아무거나 다 연결되어서 오브젝트임
            HandlerMethod hm = (HandlerMethod) handler;

        }
        log.info("log [{}][{}][{}]",uuid,requestURI,handler);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]",modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Object uuid = request.getAttribute("uuid");
        String requestURI = request.getRequestURI();

        log.info("log [{}][{}][{}]",uuid,requestURI,handler);
        if(ex != null){
            log.error("오류!:{}",ex.getMessage());
        }

    }
}
