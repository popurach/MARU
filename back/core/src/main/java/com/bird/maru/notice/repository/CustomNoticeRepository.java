package com.bird.maru.notice.repository;

import com.bird.maru.notice.model.Notice;
import java.util.List;

public interface CustomNoticeRepository {

    int bulkInsertNotices(List<Notice> notices);

}
