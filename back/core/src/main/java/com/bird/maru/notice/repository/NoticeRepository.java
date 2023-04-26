package com.bird.maru.notice.repository;

import com.bird.maru.notice.model.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoticeRepository extends MongoRepository<Notice, String> {

    Slice<Notice> findByMemberIdOrderByCreatedDateTimeDesc(Long memberId, Pageable pageable);

}
