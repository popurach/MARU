package com.shoebill.maru.model.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.myBiddings.MyBidding

class MyBiddingSource(private val myBiddingRepository: MyBiddingRepository) :
    PagingSource<Long, MyBidding>() {

    override fun getRefreshKey(state: PagingState<Long, MyBidding>): Long? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, MyBidding> {
        return try {
            val lastOffset = params.key ?: 0L

            val myBiddingResponse = myBiddingRepository.getMyBiddings(lastOffset)

            LoadResult.Page(
                data = myBiddingResponse,
                prevKey = if (lastOffset == 0L) null else myBiddingResponse.first().auctionLogId,
                nextKey = myBiddingResponse.last().auctionLogId
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}