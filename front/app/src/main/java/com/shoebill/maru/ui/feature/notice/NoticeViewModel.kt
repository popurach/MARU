package com.shoebill.maru.ui.feature.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shoebill.maru.model.data.notice.Notice
import com.shoebill.maru.model.repository.NoticeRepository
import com.shoebill.maru.model.source.NoticeSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
) : ViewModel() {
    private val _isNew = MutableLiveData<Boolean>()
    val isNew: LiveData<Boolean> get() = _isNew

    fun newNoticeArrived() {
        _isNew.value = true
    }

    fun readNotice() {
        _isNew.value = false
    }

    fun getNoticePagination(navHostController: NavHostController): Flow<PagingData<Notice>> {
        return Pager(PagingConfig(pageSize = 15)) {
            NoticeSource(noticeRepository, navHostController = navHostController)
        }.flow
    }
}