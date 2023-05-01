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

}
