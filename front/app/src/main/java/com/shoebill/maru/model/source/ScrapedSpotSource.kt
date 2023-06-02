package com.shoebill.maru.model.source

import androidx.navigation.NavHostController
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.repository.SpotRepository
import com.shoebill.maru.util.apiCallback

class ScrapedSpotSource(
    private val spotRepository: SpotRepository,
    private val navHostController: NavHostController,
) : PagingSource<Any, Spot>() {
    override fun getRefreshKey(state: PagingState<Any, Spot>): Any? {
        val lastItem = state.lastItemOrNull()

        return lastItem?.id?.toInt()
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, Spot> {
        return try {
            val spotResponse = apiCallback(navHostController) {
                spotRepository.getMyScrapedSpots(lastOffset = params.key as Long?)
            } ?: listOf()

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