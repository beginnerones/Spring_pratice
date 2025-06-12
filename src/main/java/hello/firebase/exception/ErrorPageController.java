package hello.firebase.exception;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE;

@Controller
@Slf4j
public class ErrorPageController {

    @RequestMapping(value="/error-page/500",produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> error500(HttpServletRequest request, HttpServletResponse response){
        log.info("API errorPage 500");

        Map<String,Object> result=new HashMap<>();
        //에러 따로 빼기.
        Exception ex =(Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status",request.getAttribute(ERROR_STATUS_CODE));
        result.put("message",ex.getMessage());
        Integer statusCode =(Integer) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));

    }
}
