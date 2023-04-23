package com.bird.maru.notice.service;

import com.bird.maru.domain.model.entity.Member;
import com.bird.maru.notice.model.Message;
import com.bird.maru.notice.model.Notice;
import java.util.List;

public interface NoticeService {

    List<Notice> findByMemberId(Long memberId);

    void saveNotice(Member member, Message message);

    void saveBulkNotice(List<Member> members, List<Message> messages);


}
