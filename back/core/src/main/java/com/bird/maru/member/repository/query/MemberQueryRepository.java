package com.bird.maru.member.repository.query;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.domain.model.type.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberQueryRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndProvider(String email, Provider provider);

}
