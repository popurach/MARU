package com.shoebill.maru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoebill.maru.model.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.pow


@HiltViewModel
class AuctionViewModel @Inject constructor(private val auctionRepository: AuctionRepository) :
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

    init {
//        val auctionInfo = auctionRepository.getAuctionInfo(0)
    }

    fun increaseBid() {
        _bid.value = _bid.value!!.plus(unit)
    }

    fun decreaseBid() {
        _bid.value = _bid.value!!.minus(unit)
    }
}