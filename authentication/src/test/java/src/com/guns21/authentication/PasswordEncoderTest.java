package src.com.guns21.authentication;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class PasswordEncoderTest {
    private static final RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('a', 'z')
            .withinRange('A', 'Z')
            .withinRange(0, 9)
            .build();
    public static void main(String[] args) throws Exception {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(8);
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder("8iekd,a.oa0923.",18500,256);


        long st = System.currentTimeMillis();
        final int max = 100;
        for (int i = 0; i < max; i++) {

            System.err.println(pbkdf2PasswordEncoder.encode("dsafasd").length());
//            System.err.println(UserEncrypt.encryptUserPassword(11,"ddddd"));
        }
        long et = System.currentTimeMillis();
        System.out.println(1000 * max / (et - st) + "/s");
//        System.out.println( pbkdf2PasswordEncoder.matches("dsafasd", "cbd15d27bd0293d03ecb19551e064dbf9589c3c729a43b0d88c469048e02c39df819c159dc478be6"));

    }
}
