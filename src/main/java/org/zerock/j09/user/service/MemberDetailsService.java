package org.zerock.j09.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zerock.j09.user.dto.MemberDTO;
import org.zerock.j09.user.entity.Member;
import org.zerock.j09.user.repository.MemberRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("===================================");
        log.info("username: " + username);

        Optional<Member> op = memberRepository.findByEmail(username);

        if(op.isPresent()){

            Member member = op.get();
            UserDetails result = new MemberDTO(
                    username,
                    member.getMpw(),
                    member.getMemberRoleSet().stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toList()) );

            return result;
        }

        return null;
    }
}
