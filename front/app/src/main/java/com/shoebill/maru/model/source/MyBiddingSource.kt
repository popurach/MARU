package com.shoebill.maru.model.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.myAuction.MyAuction
import com.shoebill.maru.model.repository.MyBiddingRepository

class MyBiddingSource(private val myBiddingRepository: MyBiddingRepository) :
    PagingSource<Any, MyAuction>() {

    override fun getRefreshKey(state: PagingState<Any, MyAuction>): Long? {
        return null
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, MyAuction> {
        return try {
            val myBiddingResponse = myBiddingRepository.getMyBiddings(params.key as Long?)

            val nextKey = myBiddingResponse.lastOrNull()?.id ?: return LoadResult.Page(
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