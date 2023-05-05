package com.shoebill.maru.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shoebill.maru.model.data.Spot
import com.shoebill.maru.model.data.Stamp
import com.shoebill.maru.model.repository.GallerySource
import com.shoebill.maru.model.repository.LandmarkRepository
import com.shoebill.maru.model.repository.ScrapedSpotSource
import com.shoebill.maru.model.repository.SpotRepository
import com.shoebill.maru.model.repository.StampSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val spotRepository: SpotRepository,
    private val landmarkRepository: LandmarkRepository
) : ViewModel() {
    private val _tabIndex = MutableLiveData<Int>(0)

    val tabIndex: LiveData<Int> get() = _tabIndex

    fun getScrapedSpotsPagination(): Flow<PagingData<Spot>> {
        return Pager(PagingConfig(pageSize = 20)) {
            ScrapedSpotSource(spotRepository)
        }.flow
    }

    fun getGalleryPagination(): Flow<PagingData<Spot>> {
        return Pager(PagingConfig(pageSize = 20)) {
            GallerySource(spotRepository)
        }.flow
    }

    fun getStampPagination(): Flow<PagingData<Stamp>> {
        return Pager(PagingConfig(pageSize = 20)) {
            StampSource(landmarkRepository)
        }.flow
    }

    fun switchTab(next: Int) {
        _tabIndex.value = next
    }
}