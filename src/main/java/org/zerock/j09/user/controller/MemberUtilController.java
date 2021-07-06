package org.zerock.j09.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberUtilController {

    @GetMapping("/aaa")
    public String[] test() {

        return new String[] {"AAA"};
    }
}
