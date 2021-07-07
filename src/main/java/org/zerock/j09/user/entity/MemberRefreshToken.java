package org.zerock.j09.user.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class MemberRefreshToken {

    @Id
    private String email;

    private String refreshStr;

    private long expireDate;
}
