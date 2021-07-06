package org.zerock.j09;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;

@SpringBootTest
class J09ApplicationTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    public void testEncode() {

        System.out.println(passwordEncoder.encode("1111"));

        String enStr = "$2a$10$s56SRk9i6Ta73Fs7s9AbkOMZC4nThNmFl49d76Zzv5zmE4c43WNgu";

        System.out.println(passwordEncoder.matches("1111",enStr));

    }

    @Test
    public void testMatch() {

        String pattern = "/member/**";

        AntPathMatcher matcher = new AntPathMatcher();

        boolean matchResult = matcher.match(pattern, "/member/aaa/bdae");


        System.out.println("============================");
        System.out.println(matchResult);



    }
}
