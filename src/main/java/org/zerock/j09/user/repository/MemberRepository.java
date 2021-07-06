package org.zerock.j09.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.j09.user.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = "memberRoleSet", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.email = :email ")
    Optional<Member> findByEmail(String email);

}
