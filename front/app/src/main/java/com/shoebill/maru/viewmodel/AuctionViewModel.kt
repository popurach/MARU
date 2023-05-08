package com.shoebill.maru.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoebill.maru.model.data.AuctionBiddingRequest
import com.shoebill.maru.model.data.AuctionInfo
import com.shoebill.maru.model.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow


@HiltViewModel
class AuctionViewModel @Inject constructor(
    private val auctionRepository: AuctionRepository,
) :
    ViewModel() {
    private val _bid = MutableLiveData<Int>(23000)
    val bid get() = _bid

    val unit: Int
        get() {
            val divideValue = _bid.value?.div(10)
            val stringValue = divideValue.toString()
            val lengthValue = stringValue.length - 1

            return 10.0.pow(lengthValue.toDouble()).toInt()
        }

    val downPrice: Int get() = if (_bid.value == null) 1000 else _bid.value!!.minus(unit)
    val upPrice: Int get() = if (_bid.value == null) 1000 else _bid.value!!.plus(unit)

    private val _auctionHistory = MutableLiveData<Array<Int>>()
    val auctionHistory: LiveData<Array<Int>> = _auctionHistory

    private val _auctionInfo = MutableLiveData<AuctionInfo>()
    val auctionInfo: LiveData<AuctionInfo> = _auctionInfo

    private val _biddingPrice = MutableLiveData<Int>()
    val biddingPrice: LiveData<Int> = _biddingPrice

    private var landmarkId: Long = 2

    fun initLandmarkId(value: Long) {
        landmarkId = value
    }

    fun increaseBid() {
        _bid.value = _bid.value!!.plus(unit)
    }

    fun decreaseBid() {
        _bid.value = _bid.value!!.minus(unit)
    }

//    init {
//        Log.d("AUCTION", "landmarkId: $landmarkId")
//        getAuctionHistory(landmarkId)
//        getAuctionInfo(landmarkId)
//    }

    fun getAuctionHistory(landmarkId: Long) {
        viewModelScope.launch {
            try {
                val result = auctionRepository.getAuctionHistory(landmarkId).toTypedArray()
                _auctionHistory.value = if (result.isEmpty()) arrayOf(2, 4, 2, 5, 2) else result
                Log.d(
                    "AUCTION",
                    "getAuctionHistory: ${_auctionHistory.value.contentDeepToString()}"
                )
            } catch (e: Exception) {
                Log.e("AUCTION", "Error while getting auction info: $e")
            }
        }
    }

    fun getAuctionInfo(landmarkId: Long) {
        viewModelScope.launch {
            try {
                val result = auctionRepository.getAuctionInfo(landmarkId)
                _auctionInfo.value = result
                Log.d("AUCTION", "getAuctionInfo: ${_auctionInfo.value}")
            } catch (e: Exception) {
                Log.e("AUCTION", "getAuctionInfo fail: $e")
            }
        }
    }

    fun getBiddingPrice(landmarkId: Long) {
        viewModelScope.launch {
            try {
                val result = auctionRepository.getBiddingPrice(landmarkId)
                _biddingPrice.value = result
                Log.d("AUCTION", "getBiddingPrice: ${_biddingPrice.value}")
            } catch (e: Exception) {
                Log.e("AUCTION", "getBiddingPrice fail: $e")
            }
        }
    }

    fun createBidding(onComplete: (Boolean) -> Unit) {
        val requestBody = AuctionBiddingRequest(4, _bid.value!!)
        viewModelScope.launch {
            val success = viewModelScope.async {
                auctionRepository.createBidding(requestBody)
            }.await()

            onComplete(success)
        }
    }

    fun updateBidding(onComplete: (Boolean) -> Unit) {
        val requestBody = AuctionBiddingRequest(4, _bid.value!!)
        viewModelScope.launch {
            val success = viewModelScope.async {
                auctionRepository.updateBidding(requestBody)
            }.await()

            onComplete(success)
        }
    }

    fun deleteBidding(auctionLogId: Long, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = viewModelScope.async {
                auctionRepository.deleteBidding(auctionLogId)
            }.await()

            onComplete(success)
        }
    }
}