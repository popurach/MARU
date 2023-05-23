package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.notice.Notice
import com.shoebill.maru.model.interfaces.NoticeApi
import retrofit2.Response
import javax.inject.Inject

class NoticeRepository @Inject constructor(
    private val noticeApi: NoticeApi,
) {
    suspend fun getNotices(page: Int = 0): Response<List<Notice>> = noticeApi.getNotices(page)
}