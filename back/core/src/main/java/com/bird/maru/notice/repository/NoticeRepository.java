package com.bird.maru.notice.repository;

import com.bird.maru.notice.model.Notice;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoticeRepository extends MongoRepository<Notice, String> {

    List<Notice> findByMemberId(Long memberId);

}
