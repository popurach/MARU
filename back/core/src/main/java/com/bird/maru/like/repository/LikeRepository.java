package com.bird.maru.like.repository;

import com.bird.maru.domain.model.entity.Like;
import com.bird.maru.domain.model.type.id.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {

}
