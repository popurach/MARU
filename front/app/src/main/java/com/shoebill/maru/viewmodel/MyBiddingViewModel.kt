package com.shoebill.maru.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shoebill.maru.model.data.myBiddings.LandmarkInfo
import com.shoebill.maru.model.data.myBiddings.MyBidding
import com.shoebill.maru.model.repository.MyBiddingRepository
import com.shoebill.maru.model.repository.MyBiddingSource
import com.shoebill.maru.model.repository.MyNonBiddingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MyBiddingViewModel @Inject constructor(private val myBiddingRepository: MyBiddingRepository) :
    ViewModel() {

    fun getMyBiddingPagination(): Flow<PagingData<MyBidding>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MyBiddingSource(myBiddingRepository)
        }.flow
    }

    fun getMyNonBiddingPagination(): Flow<PagingData<LandmarkInfo>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MyNonBiddingSource(myBiddingRepository)
        }.flow
    }

//    private var lastAuctionId: Long = 0
//
//    private val _myBiddings = MutableLiveData<List<MyBidding>>()
//    val myBiddings: LiveData<List<MyBidding>> get() = _myBiddings
//
//    private var nonLastAuctionId: Long = 0
//
//    private val _myNonBiddings = MutableLiveData<List<LandmarkInfo>>()
//    val myNonBiddings: LiveData<List<LandmarkInfo>> get() = _myNonBiddings
//
//    private fun getMyBiddings() {
//        viewModelScope.launch {
//            try {
//                val result = myBiddingRepository.getMyBiddings(lastAuctionId)
//
//                if (result.isNotEmpty()) {
//                    lastAuctionId = result.last().auctionLogId
//                }
//
//                _myBiddings.value = _myBiddings.value?.plus(result) ?: result
//                Log.d("MYBIDDINGS", "getMyBiddings: ${_myBiddings.value}")
//            } catch (e: Exception) {
//                Log.e("MYBIDDINGS", "getMyBiddings: $e")
//            }
//        }
//    }
//
//    private fun getMyNonBiddings() {
//        viewModelScope.launch {
//            try {
//                val result = myBiddingRepository.getMyNonBiddings(nonLastAuctionId)
//
//                if (result.isNotEmpty()) {
//                    nonLastAuctionId = result.last().landmarkId
//                }
//
//                _myNonBiddings.value = _myNonBiddings.value?.plus(result) ?: result
//                Log.d("MYNONBIDDINGS", "getMyNonBiddings: ${_myNonBiddings.value}")
//            } catch (e: Exception) {
//                Log.e("MYNONBIDDINGS", "getMyNonBiddings: $e")
//            }
//        }
//    }
}