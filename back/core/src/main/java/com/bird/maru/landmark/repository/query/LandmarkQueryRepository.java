package com.bird.maru.landmark.repository.query;

import com.bird.maru.domain.model.entity.Landmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandmarkQueryRepository extends JpaRepository<Landmark, Long> {

}
