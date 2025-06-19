package hello.firebase.util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Slf4j
@Component
public class AESUtil { //aes 암호화 클래스

    private final String ALGORITHM="AES";

    private final String SECRET_KEY;

    public AESUtil(@Value(value = "${api.secret_key}") String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    public String encrypt(String data) throws Exception {
        SecretKeySpec keySpec=new SecretKeySpec(SECRET_KEY.getBytes(),ALGORITHM); //바이트 배열을 키 객체로 변경하여 암호화에 사용할수 있게 변경.
        Cipher cipher = Cipher.getInstance(ALGORITHM);//aes에 맞는 인스턴스 생성.
        cipher.init(Cipher.ENCRYPT_MODE, keySpec); //해당 객체를 초기화
        byte[] encryptedBytes= cipher.doFinal(data.getBytes()); //암호화할 문자열을 넣어 암호문생성.
        return Base64.getEncoder().encodeToString(encryptedBytes); //아직 바이트라 전송하기 힘드니까 base64로 인코딩으로 문자 변환.
    }

    public String decrypt(String data) throws Exception {
        SecretKeySpec keySpec=new SecretKeySpec(SECRET_KEY.getBytes(),ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] encryptedBytes= Base64.getDecoder().decode(data);
        byte[] decryptedBytes= cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
