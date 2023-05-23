package com.shoebill.maru.model.source

import androidx.navigation.NavHostController
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.Stamp
import com.shoebill.maru.model.repository.LandmarkRepository
import com.shoebill.maru.util.apiCallback

class StampSource(
    private val landmarkRepository: LandmarkRepository,
    private val navHostController: NavHostController,
) : PagingSource<Any, Stamp>() {
    override fun getRefreshKey(state: PagingState<Any, Stamp>): Any? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, Stamp> {
        return try {
            val data = apiCallback(navHostController) {
                landmarkRepository.getVisitedLandmarks(lastOffset = params.key as Long?)
            } ?: listOf()
            val nextOffset = data.lastOrNull()?.landmarkId ?: return LoadResult.Page(
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