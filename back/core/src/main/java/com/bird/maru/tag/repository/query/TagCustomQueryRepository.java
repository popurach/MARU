package com.bird.maru.tag.repository.query;


import static com.bird.maru.domain.model.entity.QSpotHasTag.spotHasTag;
import static com.bird.maru.domain.model.entity.QTag.tag;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import com.bird.maru.domain.model.entity.Tag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagCustomQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Map<Long, List<Tag>> findAllWithTagBySpotIn(List<Long> spotIds) {
        return queryFactory.selectFrom(spotHasTag)
                           .join(spotHasTag.tag, tag)
                           .where(spotHasTag.spot.id.in(spotIds))
                           .transform(groupBy(spotHasTag.spot.id).as(list(spotHasTag.tag)));
    }

}
