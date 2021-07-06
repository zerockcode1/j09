package org.zerock.j09.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.j09.user.entity.Member;
import org.zerock.j09.user.entity.MemberRole;
import org.zerock.j09.user.repository.MemberRepository;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepoTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .mpw(passwordEncoder.encode("1111"))
                    .mname("USER"+i)
                    .build();
            member.addMemberRole(MemberRole.USER);

            if(i > 80){
                member.addMemberRole(MemberRole.MEMBER);
            }
            if(i > 90){
                member.addMemberRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);

        });

    }

    @Test
    public void testLoad(){

        String email = "user10@aaa.com";

        Optional<Member> result = memberRepository.findByEmail(email);

        result.ifPresent(member -> System.out.println(member));

    }


}
























