package org.zerock.j09.user.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.j09.user.dto.MemberDTO;
import org.zerock.j09.user.entity.Member;

@RestController
@RequestMapping("/sample")
@Log4j2
public class SampleController {

    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public String[] doAll(){
        return new String[]{"AAA","AAA","AAAA"};
    }

    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/member")
    public String[] doMember(){
        return new String[]{"BBB","BBB","BBB"};
    }

    @GetMapping("/admin")
    public String[] doAdmin( @AuthenticationPrincipal MemberDTO dto){

        log.info(dto);

        return new String[]{"CCC","CCC","CCC"};
    }

}
