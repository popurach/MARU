package com.shoebill.maru.model.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.landmark.SpotImage

class LandmarkImageSource(
    private val landmarkRepository: LandmarkRepository,
    private val landmarkId: Long
) : PagingSource<Any, SpotImage>() {
    override fun getRefreshKey(state: PagingState<Any, SpotImage>): Any? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, SpotImage> {
        return try {
            val response =
                landmarkRepository.getLandmarkImages(lastOffset = params.key as Long?, landmarkId = landmarkId)
            val nextOffset = response.lastOrNull()?.id ?: return LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = null
            )

            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = nextOffset,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}