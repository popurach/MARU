package com.bird.maru.spot.repository.query;

import com.bird.maru.domain.model.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotQueryRepository extends JpaRepository<Spot, Long> {

}
