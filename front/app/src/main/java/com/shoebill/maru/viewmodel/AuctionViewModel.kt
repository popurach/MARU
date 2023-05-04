package com.shoebill.maru.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoebill.maru.model.data.AuctionBiddingRequest
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

    private val _tabIndex = MutableLiveData(0)
    val tabIndex: LiveData<Int> get() = _tabIndex

    val unit: Int
        get() {
            val divideValue = _bid.value?.div(10)
            val stringValue = divideValue.toString()
            val lengthValue = stringValue.length - 1

            return 10.0.pow(lengthValue.toDouble()).toInt()
        }

    val downPrice: Int get() = if (_bid.value == null) 1000 else _bid.value!!.minus(unit)
    val upPrice: Int get() = if (_bid.value == null) 1000 else _bid.value!!.plus(unit)

    private val _auctionInfo = MutableLiveData<Array<Int>>()
    val auctionInfo: LiveData<Array<Int>> = _auctionInfo

    init {
        getAuctionInfo(2)
    }

    fun increaseBid() {
        _bid.value = _bid.value!!.plus(unit)
    }

    fun decreaseBid() {
        _bid.value = _bid.value!!.minus(unit)
    }

    fun switchTab(newIndex: Int) {
        _tabIndex.value = newIndex
    }

    private fun getAuctionInfo(landmarkId: Long) {
        viewModelScope.launch {
            try {
                val result = auctionRepository.getAuctionInfo(landmarkId).toTypedArray()
                _auctionInfo.value = if (result.isEmpty()) arrayOf(2, 4, 2, 5, 2) else result
                Log.d("AUCTION", "getAuctionInfo: ${_auctionInfo.value}")
            } catch (e: Exception) {
                Log.e("AUCTION", "Error while getting auction info: $e")
            }
        }
    }

    fun createBidding(onComplete: (Boolean) -> Unit) {
        val requestBody = AuctionBiddingRequest(6, _bid.value!!)
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