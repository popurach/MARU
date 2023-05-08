package com.shoebill.maru.model.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.myBiddings.MyBidding

class MyBiddingSource(private val myBiddingRepository: MyBiddingRepository) :
    PagingSource<Any, MyBidding>() {

    override fun getRefreshKey(state: PagingState<Any, MyBidding>): Long? {
        return null
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, MyBidding> {
        return try {
            val myBiddingResponse = myBiddingRepository.getMyBiddings(params.key as Long?)

            val nextKey = myBiddingResponse.lastOrNull()?.auctionLogId ?: return LoadResult.Page(
                data = myBiddingResponse,
                prevKey = null,
                nextKey = null
            )

            LoadResult.Page(
                data = myBiddingResponse,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}