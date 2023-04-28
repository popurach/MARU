package com.shoebill.maru.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shoebill.maru.model.data.notice.Notice
import com.shoebill.maru.model.repository.NoticeRepository
import com.shoebill.maru.model.repository.NoticeSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository
) : ViewModel() {
    // 전역적으로 관리 되어야 하는거?
    fun getNoticePagination(): Flow<PagingData<Notice>> {
        return Pager(PagingConfig(pageSize = 15)) {
            NoticeSource(noticeRepository)
        }.flow
    }
}