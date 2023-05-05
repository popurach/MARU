package com.bird.maru.tag.repository;

import com.bird.maru.domain.model.entity.SpotHasTag;
import com.bird.maru.domain.model.type.id.SpotHasTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotHasTagRepository extends JpaRepository<SpotHasTag, SpotHasTagId> {

}
