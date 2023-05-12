package com.shoebill.maru.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shoebill.maru.model.data.myAuction.LandmarkInfo
import com.shoebill.maru.model.data.myAuction.MyAuction
import com.shoebill.maru.model.repository.MyBiddingRepository
import com.shoebill.maru.model.repository.MyBiddingSource
import com.shoebill.maru.model.repository.MyNonBiddingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyBiddingViewModel @Inject constructor(private val myBiddingRepository: MyBiddingRepository) :
    ViewModel() {

    fun getMyBiddingPagination(): Flow<PagingData<MyAuction>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MyBiddingSource(myBiddingRepository)
        }.flow
    }

    fun getMyNonBiddingPagination(): Flow<PagingData<LandmarkInfo>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MyNonBiddingSource(myBiddingRepository)
        }.flow
    }
}