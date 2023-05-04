package com.shoebill.maru.model.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.Spot

class ScrapedSpotSource(private val spotRepository: SpotRepository) : PagingSource<Any, Spot>() {
    override fun getRefreshKey(state: PagingState<Any, Spot>): Any? {
        val lastItem = state.lastItemOrNull()

        return lastItem?.id?.toInt()
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, Spot> {
        return try {
            val spotResponse = spotRepository.getMyScrapedSpots(lastOffset = params.key as Long?)
            val nextOffset = spotResponse.lastOrNull()?.id ?: return LoadResult.Page(
                data = spotResponse,
                prevKey = null,
                nextKey = null
            )

            LoadResult.Page(
                data = spotResponse,
                prevKey = null,
                nextKey = nextOffset,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}