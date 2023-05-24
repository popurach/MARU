package com.shoebill.maru.ui.feature.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.data.Stamp
import com.shoebill.maru.model.repository.LandmarkRepository
import com.shoebill.maru.model.repository.SpotRepository
import com.shoebill.maru.model.source.GallerySource
import com.shoebill.maru.model.source.ScrapedSpotSource
import com.shoebill.maru.model.source.StampSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val spotRepository: SpotRepository,
    private val landmarkRepository: LandmarkRepository,
) : ViewModel() {
    private val _tabIndex = MutableLiveData<Int>(0)

    val tabIndex: LiveData<Int> get() = _tabIndex

    fun getScrapedSpotsPagination(navHostController: NavHostController): Flow<PagingData<Spot>> {
        return Pager(PagingConfig(pageSize = 20)) {
            ScrapedSpotSource(spotRepository, navHostController = navHostController)
        }.flow
    }

    fun getGalleryPagination(navHostController: NavHostController): Flow<PagingData<Spot>> {
        return Pager(PagingConfig(pageSize = 20)) {
            GallerySource(spotRepository, navHostController = navHostController)
        }.flow
    }

    fun getStampPagination(navHostController: NavHostController): Flow<PagingData<Stamp>> {
        return Pager(PagingConfig(pageSize = 20)) {
            StampSource(landmarkRepository, navHostController = navHostController)
        }.flow
    }

    fun switchTab(next: Int) {
        _tabIndex.value = next
    }
}