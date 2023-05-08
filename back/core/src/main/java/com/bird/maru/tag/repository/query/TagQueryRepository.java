package com.bird.maru.tag.repository.query;

import com.bird.maru.domain.model.entity.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagQueryRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT t FROM Tag t WHERE t.name in :names")
    List<Tag> findAllByNames(@Param("names") List<String> names);

}
