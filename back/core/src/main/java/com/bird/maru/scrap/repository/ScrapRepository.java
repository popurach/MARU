package com.bird.maru.scrap.repository;

import com.bird.maru.domain.model.entity.Scrap;
import com.bird.maru.domain.model.type.id.ScrapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, ScrapId> {

}
