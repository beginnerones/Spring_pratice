package hello.firebase.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    TOKEN_EXPIRED(403,444,"토큰이 만료되었습니다."),
    INVALID_TOKEN(400,400,"유효하지 않은 토큰입니다."),
    NOT_EXIST_TOKEN(400,400,"두개의 토큰이 모두 존재하지 않습니다."),
    EXCEPTION(400,400,"오류가 발생했습니다");

    private final Integer status;
    private final Integer code;
    private final String message;
}
