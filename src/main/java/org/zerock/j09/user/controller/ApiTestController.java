package org.zerock.j09.user.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.j09.user.dto.MemberDTO;

import java.security.Principal;



@RestController
@RequestMapping("/api/board")
@Log4j2


public class ApiTestController {

    @GetMapping("/list")
    public String[] getList(@AuthenticationPrincipal MemberDTO memberDTO) {

        log.info("-------------------" + memberDTO);

        log.info("username: "+memberDTO.getUsername());

        log.info("password: " + memberDTO.getPassword());

        return new String[]{"CCC","CCC","CCC"};
    }

    @PreAuthorize("principal.username == #writer")
    @GetMapping("/delete")
    public String[] delete(Long bno, String writer) {

        log.info("delete: " + bno);
        log.info("writer: " + writer);

        return new String[]{"DEL","DEL","DEL"};
    }
}
