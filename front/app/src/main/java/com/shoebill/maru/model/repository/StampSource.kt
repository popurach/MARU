package com.shoebill.maru.model.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.Stamp

class StampSource(private val landmarkRepository: LandmarkRepository) : PagingSource<Any, Stamp>() {
    override fun getRefreshKey(state: PagingState<Any, Stamp>): Any? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, Stamp> {
        return try {
            val stampsResponse =
                landmarkRepository.getVisitedLandmarks(lastOffset = params.key as Long?)
            val nextOffset = stampsResponse.lastOrNull()?.landmarkId ?: return LoadResult.Page(
                data = stampsResponse,
                prevKey = null,
                nextKey = null
            )

            LoadResult.Page(
                data = stampsResponse,
                prevKey = null,
                nextKey = nextOffset,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}