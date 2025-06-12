package hello.firebase.service;

import hello.firebase.dao.TestDto;
import hello.firebase.domain.User;
import hello.firebase.domain.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserdetailsService implements UserDetailsService {
    private final TestDto testDto;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user=testDto.find(username).orElseThrow(() -> new Exception("멤버 못찾음 ㅋㅋ"));
            return new UserPrincipal(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
