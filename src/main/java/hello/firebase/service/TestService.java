package hello.firebase.service;

import hello.firebase.dao.TestDto;
import hello.firebase.domain.User;
import hello.firebase.dto.UserDto;
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

    public TestService(TestDto testDto) {
        this.testDto = testDto;
    }

    @Transactional
    public User saveUser(UserDto userDto) {
        User user = new User();
        user.setUser(userDto.getUser());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        testDto.save(user);
        return user;
    }

    @Transactional
    public User findByIdentifier(String email){
        Optional<User>user= testDto.findByIdentifier(email);
        return user.get();
    }


}
