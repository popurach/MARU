package com.shoebill.maru.model.source

import androidx.navigation.NavHostController
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.landmark.SpotImage
import com.shoebill.maru.model.repository.LandmarkRepository
import com.shoebill.maru.util.apiCallback

class LandmarkImageSource(
    private val landmarkRepository: LandmarkRepository,
    private val landmarkId: Long,
    private val navHostController: NavHostController,
) : PagingSource<Any, SpotImage>() {
    override fun getRefreshKey(state: PagingState<Any, SpotImage>): Any? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, SpotImage> {
        return try {
            val data = apiCallback(navHostController) {
                landmarkRepository.getLandmarkImages(
                    lastOffset = params.key as Long?,
                    landmarkId = landmarkId
                )
            } ?: listOf()
            val nextOffset = data.lastOrNull()?.id ?: return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = null
            )

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextOffset,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}