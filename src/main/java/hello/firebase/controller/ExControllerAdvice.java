package hello.firebase.controller;

import hello.firebase.exception.UserException;
import hello.firebase.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//예외 처리에 대해서 때어서 놓음.
@RestControllerAdvice
@Slf4j
public class ExControllerAdvice {
    //예가 대신 처리가 된다.아래 API들 전부 합쳐서 ILLEGAL오류 발생시 처리해줌.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandler(IllegalArgumentException ex){
        log.info("illegalExHandler");
        //정상적으로 리턴해줌.그런데 200코드로 반환해줌.문제.
        return new ErrorResult("BAD",ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException ex){
        log.error("userExHandler e",ex);
        ErrorResult errorResult = new ErrorResult("USER-EX", ex.getMessage());
        return new ResponseEntity(errorResult,HttpStatus.BAD_REQUEST);
    }

    //이게 최상위라 다른 예외가 처리못하는걸 여기서 전부 처리한다.
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception ex){
        log.error("exHandler e",ex);
        return new ErrorResult("EX",ex.getMessage());
    }

}
