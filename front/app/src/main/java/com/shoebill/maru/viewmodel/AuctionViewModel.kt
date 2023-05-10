package com.shoebill.maru.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoebill.maru.model.data.AuctionBiddingRequest
import com.shoebill.maru.model.data.AuctionInfo
import com.shoebill.maru.model.repository.AuctionRepository
import com.shoebill.maru.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject
import kotlin.math.pow


@HiltViewModel
class AuctionViewModel @Inject constructor(
    private val auctionRepository: AuctionRepository,
) :
    ViewModel() {

    private val _currentHighestBid = MutableLiveData<Int>(10000)
    val currentHighestBid: LiveData<Int> = _currentHighestBid

//    private val _biddingPrice = MutableLiveData<Int>(10000)
//    val biddingPrice: LiveData<Int> = _biddingPrice

    private val _bid = MutableLiveData<Int>(1000)
    val bid: LiveData<Int> = _bid

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

    private var landmarkId: Long = 2

    private var isLoading = false

    fun initLandmarkId(value: Long, context: Context) {
        if (isLoading) return
        isLoading = true
        landmarkId = value
        viewModelScope.launch {
            getAuctionHistory(landmarkId)
            getAuctionInfo(landmarkId)
//            getBiddingPrice(landmarkId)
            runStomp(context)
        }
    }

    fun increaseBid() {
        _bid.value = _bid.value!!.plus(unit)
    }

    fun decreaseBid() {
        _bid.value = _bid.value!!.minus(unit)
    }

    private fun getAuctionHistory(landmarkId: Long) {
        viewModelScope.launch {
            try {
                val result = auctionRepository.getAuctionHistory(landmarkId).toTypedArray()
                val modifiedList = mutableListOf<Int>()
                if (result.size == 1) {
                    modifiedList.add(0)
                    modifiedList.addAll(result)
                }
                _auctionHistory.value =
                    if (result.isEmpty()) arrayOf(
                        10000,
                        13000,
                        20000,
                        26000
                    ) else modifiedList.toTypedArray()
                Log.d(
                    "AUCTION",
                    "getAuctionHistory: ${_auctionHistory.value.contentDeepToString()}"
                )
            } catch (e: Exception) {
                Log.e("AUCTION", "Error while getting auction info: $e")
            }
        }
    }

    private fun getAuctionInfo(landmarkId: Long) {
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

//    private fun getBiddingPrice(landmarkId: Long) {
//        viewModelScope.launch {
//            try {
//                val result = auctionRepository.getBiddingPrice(landmarkId)
//                _biddingPrice.value = result
//                _bid.value = result
//                _bid.value = result.plus(unit)
//                Log.d("AUCTION", "getBiddingPrice: ${_biddingPrice.value}")
//            } catch (e: Exception) {
//                Log.e("AUCTION", "getBiddingPrice fail: $e")
//            }
//        }
//    }

    fun createBidding(onComplete: (Boolean) -> Unit) {
        val requestBody = AuctionBiddingRequest(landmarkId, _bid.value!!)
        viewModelScope.launch {
            val success = viewModelScope.async {
                auctionRepository.createBidding(requestBody)
            }.await()

            onComplete(success)
        }
    }

    fun updateBidding(onComplete: (Boolean) -> Unit) {
        val requestBody = AuctionBiddingRequest(landmarkId, _bid.value!!)
        viewModelScope.launch {
            val success = viewModelScope.async {
                auctionRepository.updateBidding(requestBody)
            }.await()

            onComplete(success)
        }
    }

    fun deleteBidding(auctionLogId: Long, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = withContext(viewModelScope.coroutineContext) {
                auctionRepository.deleteBidding(auctionLogId)
            }

            onComplete(success)
        }
    }

    fun exit() {
        isLoading = false
    }

    @SuppressLint("CheckResult")
    private fun runStomp(context: Context) {
        val endpointUrl = "ws://k8a403.p.ssafy.io:8080/socket"

        val prefUtil = PreferenceUtil(context)
        val accessToken = prefUtil.getString("accessToken")
        val tokenInfo = "Bearer $accessToken"

        val headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = tokenInfo
        val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, endpointUrl, headers)

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.i("OPEND", "!!")

                    val data = JSONObject()
                    data.put("price", 0)
                    data.put("landmarkId", landmarkId)

                    stompClient.send("/bid/bid", data.toString()).subscribe(
                        {
                            Log.i("Send Success", "Message sent successfully")
                        },
                        { error ->
                            Log.e("Send Error", "Failed to send message", error)
                        })
                }

                LifecycleEvent.Type.CLOSED -> {
                    Log.i("CLOSED", "!!")

                }

                LifecycleEvent.Type.ERROR -> {
                    Log.i("ERROR", "!!")
                    Log.e("CONNECT ERROR", lifecycleEvent.exception.toString())
                }

                else -> {
                    Log.i("ELSE", lifecycleEvent.message)
                }
            }
        }

        stompClient.connect()

        stompClient.topic("/bidding/price").subscribe({ topicMessage ->
            val body = JSONObject(topicMessage.payload)
            val price = body.getInt("price")
            Log.i("Received Message", "price: $price")
            _currentHighestBid.postValue(price)
            _bid.postValue(price)
            _bid.postValue(price.plus(unit))
        }, { error ->
            Log.e("Subscription Error", "Failed to subscribe to topic", error)
        })
    }
}