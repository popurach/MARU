package com.bird.maru.scrap.repository.query;

import com.bird.maru.domain.model.entity.Scrap;
import com.bird.maru.domain.model.type.id.ScrapId;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapQueryRepository extends JpaRepository<Scrap, ScrapId> {

    @Query("SELECT s.spot.id FROM Scrap s WHERE s.deleted = FALSE AND s.member.id = :memberId AND s.spot.id IN :spotIds")
    Set<Long> findSpotIdsByMemberAndSpotIn(@Param("memberId") Long memberId, @Param("spotIds") List<Long> spotIds);

}
