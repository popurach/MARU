package com.shoebill.maru.model.source

import androidx.navigation.NavHostController
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.notice.Notice
import com.shoebill.maru.model.repository.NoticeRepository
import com.shoebill.maru.util.apiCallback

class NoticeSource(
    private val noticeRepository: NoticeRepository,
    private val navHostController: NavHostController,
) : PagingSource<Int, Notice>() {
    override fun getRefreshKey(state: PagingState<Int, Notice>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notice> {
        return try {
            val page = params.key ?: 0

            val data = apiCallback(navHostController) {
                noticeRepository.getNotices(page)
            } ?: listOf()

            LoadResult.Page(
                data = data,
                prevKey = if (page == 0) null else page - 1,
                nextKey = page.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


}