package org.zerock.j09.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.j09.user.entity.MemberRefreshToken;

import java.util.Optional;

public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {

    Optional<MemberRefreshToken> findFirstByRefreshStr(String refreshStr);
}
