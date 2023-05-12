package com.shoebill.maru.model.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.myAuction.LandmarkInfo

class MyNonBiddingSource(private val myBiddingRepository: MyBiddingRepository) :
    PagingSource<Long, LandmarkInfo>() {

    override fun getRefreshKey(state: PagingState<Long, LandmarkInfo>): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, LandmarkInfo> {
        return try {
            val lastOffset = params.key ?: 0L

            val myNonBiddingResponse = myBiddingRepository.getMyNonBiddings(lastOffset)

            LoadResult.Page(
                data = myNonBiddingResponse,
                prevKey = if (lastOffset == 0L) null else myNonBiddingResponse.first().id,
                nextKey = myNonBiddingResponse.last().id
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}