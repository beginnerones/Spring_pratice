package hello.firebase.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/member/{id}")
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

    @GetMapping("/api/response-status-ex1")
    public String requestStatus(){
        throw new BadRequestException();
    }

    @GetMapping("/api/response-status-ex2")
    public String requestStatus2(){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "error.bad",new IllegalArgumentException());

    }

    @GetMapping("/api/defalut-handler-ex3")
    public String defalutHandlerEx3(@RequestParam Integer data){
        return "ok";
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }
}
