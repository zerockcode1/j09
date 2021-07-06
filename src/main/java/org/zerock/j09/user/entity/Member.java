package org.zerock.j09.user.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    private String email;

    private String mname;

    private String mpw;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> memberRoleSet = new HashSet<>();

    public void addMemberRole(MemberRole role){
        memberRoleSet.add(role);
    }

}
