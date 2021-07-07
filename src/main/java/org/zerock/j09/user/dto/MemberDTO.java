package org.zerock.j09.user.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class MemberDTO extends User {

    public MemberDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    @Override
    public String toString() {
        return "MemberDTO{}" + super.toString();
    }
}
