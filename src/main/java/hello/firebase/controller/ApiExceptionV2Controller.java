package hello.firebase.controller;

import hello.firebase.exception.ApiExceptionController;
import hello.firebase.exception.UserException;
import hello.firebase.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class ApiExceptionV2Controller {


    @GetMapping("/api/member/V2/{id}")
    public MemberDto getMember(@PathVariable("id") String id){

        if(id.equals("ex")){
            throw new RuntimeException("예외 발생");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 파라미터");
        }

        if(id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }
        return new MemberDto(id,"hello"+id);
    }


    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }
}
