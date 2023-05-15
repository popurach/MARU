package com.shoebill.maru.model.source

import androidx.navigation.NavHostController
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.interfaces.SpotApi
import com.shoebill.maru.util.apiCallback

class SpotSource(
    private val spotApi: SpotApi,
    private val navController: NavHostController,
    private val west: Double,
    private val south: Double,
    private val east: Double,
    private val north: Double,
    private val filter: String,
    private val tagId: Long?,
) : PagingSource<Any, Spot>() {
    override fun getRefreshKey(state: PagingState<Any, Spot>): Any? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Any>): LoadResult<Any, Spot> {
        return try {
            val data = apiCallback(navController) {
                spotApi.getAroundSpots(
                    west = west,
                    south = south,
                    east = east,
                    north = north,
                    filter = filter,
                    tagId = tagId,
                    lastOffset = params.key as Long?
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
                nextKey = nextOffset
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}