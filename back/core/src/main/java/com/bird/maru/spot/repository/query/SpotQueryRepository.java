package com.bird.maru.spot.repository.query;

import com.bird.maru.domain.model.entity.Spot;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotQueryRepository extends JpaRepository<Spot, Long> {

    @Query("SELECT s.image.url "
            + "FROM Spot s "
            + "WHERE s.member.id=:memberId "
            + "AND s.landmark.id=:landmarkId "
            + "AND s.deleted=false "
            + "AND s.createdDateTime between :startDateTime AND :endDateTime")
    List<String> findOwnerSpots(
            @Param("memberId") Long memberId, @Param("landmarkId") Long landmarkId,
            @Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime);

}
