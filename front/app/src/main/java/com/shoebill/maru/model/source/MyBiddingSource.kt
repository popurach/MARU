package com.shoebill.maru.model.source

import androidx.navigation.NavHostController
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.myAuction.MyAuction
import com.shoebill.maru.model.repository.MyBiddingRepository
import com.shoebill.maru.util.apiCallback

class MyBiddingSource(
    private val myBiddingRepository: MyBiddingRepository,
    private val navHostController: NavHostController,
) :
    PagingSource<Any, MyAuction>() {

    override fun getRefreshKey(state: PagingState<Any, MyAuction>): Long? {
        return null
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, MyAuction> {
        return try {
            val data = apiCallback(navHostController) {
                myBiddingRepository.getMyBiddings(params.key as Long?)
            } ?: listOf()
            val nextKey = data.lastOrNull()?.id ?: return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = null
            )

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}