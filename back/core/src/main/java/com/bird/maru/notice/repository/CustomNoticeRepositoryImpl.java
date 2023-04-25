package com.bird.maru.notice.repository;

import com.bird.maru.notice.model.Notice;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomNoticeRepositoryImpl implements CustomNoticeRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public int bulkInsertNotices(List<Notice> notices) {
        final BulkOperations bulkInsertion = mongoTemplate.bulkOps(BulkMode.UNORDERED, Notice.class);

        bulkInsertion.insert(notices);

        return bulkInsertion.execute().getInsertedCount();
    }

}
