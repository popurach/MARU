package com.bird.maru.like.repository.query;

import com.bird.maru.domain.model.entity.Like;
import com.bird.maru.domain.model.type.id.LikeId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeQueryRepository extends JpaRepository<Like, LikeId> {

    @Query("SELECT l.spot.id FROM Like l WHERE l.deleted = FALSE AND l.member.id = :memberId AND l.spot.id IN :spotIds")
    Set<Long> findSpotIdsByMemberAndSpotIn(@Param("memberId") Long memberId, @Param("spotIds") List<Long> spotIds);

    @Query("SELECT l FROM Like l JOIN FETCH l.spot WHERE l.member.id = :memberId AND l.spot.id = :spotId")
    Optional<Like> findByMemberAndSpot(Long memberId, Long spotId);

}
