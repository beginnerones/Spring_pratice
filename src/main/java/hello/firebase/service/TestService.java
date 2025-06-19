package hello.firebase.service;

import hello.firebase.dao.TestDto;
import hello.firebase.domain.User;
import hello.firebase.dto.UserDto;
import hello.firebase.util.AESUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Getter
@Setter
@Service
public class TestService {

    TestDto testDto;
    private final AESUtil aesUtil;

    public TestService(TestDto testDto, AESUtil aesUtil) {
        this.testDto = testDto;
        this.aesUtil = aesUtil;
    }

    @Transactional
    public User saveUser(UserDto userDto)throws Exception {
        User user = new User();
        user.setUser(userDto.getUser());
        user.setAge(userDto.getAge());
        String secu_email=aesUtil.encrypt(userDto.getEmail());
        user.setEmail(secu_email);
        testDto.save(user);
        return user;
    }

    @Transactional
    public User findByIdentifier(String email){
        Optional<User>user= testDto.findByIdentifier(email);
        return user.get();
    }


}
