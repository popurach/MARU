package com.shoebill.maru.model.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.repository.SpotRepository

class GallerySource(private val spotRepository: SpotRepository) : PagingSource<Any, Spot>() {
    override fun getRefreshKey(state: PagingState<Any, Spot>): Any? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, Spot> {
        return try {
            val spotResponse = spotRepository.getMySpots(lastOffset = params.key as Long?)
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