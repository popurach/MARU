package com.shoebill.maru.ui.feature.mypage.auction

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shoebill.maru.model.data.myAuction.LandmarkInfo
import com.shoebill.maru.model.data.myAuction.MyAuction
import com.shoebill.maru.model.repository.MyBiddingRepository
import com.shoebill.maru.model.source.MyBiddingSource
import com.shoebill.maru.model.source.MyNonBiddingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyBiddingViewModel @Inject constructor(private val myBiddingRepository: MyBiddingRepository) :
    ViewModel() {

    fun getMyBiddingPagination(navHostController: NavHostController): Flow<PagingData<MyAuction>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MyBiddingSource(myBiddingRepository, navHostController = navHostController)
        }.flow
    }

    fun getMyNonBiddingPagination(navHostController: NavHostController): Flow<PagingData<LandmarkInfo>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MyNonBiddingSource(myBiddingRepository, navHostController = navHostController)
        }.flow
    }
}