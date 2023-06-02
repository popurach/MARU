package com.shoebill.maru.model.source

import androidx.navigation.NavHostController
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.myAuction.LandmarkInfo
import com.shoebill.maru.model.repository.MyBiddingRepository
import com.shoebill.maru.util.apiCallback

class MyNonBiddingSource(
    private val myBiddingRepository: MyBiddingRepository,
    private val navHostController: NavHostController,
) :
    PagingSource<Long, LandmarkInfo>() {

    override fun getRefreshKey(state: PagingState<Long, LandmarkInfo>): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, LandmarkInfo> {
        return try {
            val lastOffset = params.key ?: 0L

            val data = apiCallback(navHostController) {
                myBiddingRepository.getMyNonBiddings(lastOffset = lastOffset)
            } ?: listOf()
            LoadResult.Page(
                data = data,
                prevKey = if (lastOffset == 0L) null else data.first().id,
                nextKey = data.last().id
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}