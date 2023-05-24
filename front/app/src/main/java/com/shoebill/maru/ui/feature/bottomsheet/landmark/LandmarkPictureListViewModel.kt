package com.shoebill.maru.ui.feature.bottomsheet.landmark

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shoebill.maru.model.data.landmark.SpotImage
import com.shoebill.maru.model.repository.LandmarkRepository
import com.shoebill.maru.model.source.LandmarkImageSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LandmarkPictureListViewModel @Inject constructor(
    private val landmarkRepository: LandmarkRepository,
) : ViewModel() {
    private var landmarkId: Long = 0

    fun initLandmarkId(value: Long) {
        landmarkId = value
    }

    fun getLandmarkPicturePagination(navHostController: NavHostController): Flow<PagingData<SpotImage>> {
        return Pager(PagingConfig(pageSize = 20)) {
            LandmarkImageSource(landmarkRepository, landmarkId, navHostController)
        }.flow
    }
}