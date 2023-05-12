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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import javax.inject.Inject
import kotlin.math.pow


@HiltViewModel
class AuctionViewModel @Inject constructor(
    private val auctionRepository: AuctionRepository,
) :
    ViewModel() {

    private val _currentHighestBid = MutableLiveData<Int>(0)
    val currentHighestBid: LiveData<Int> = _currentHighestBid

    private val _bid = MutableLiveData<Int>(0)
    val bid: LiveData<Int> = _bid

    private val _unit = MutableLiveData<Int>(1000)
    val unit: LiveData<Int> = _unit

    val downPrice: Int get() = if (_bid.value == null) 1000 else _bid.value!!.minus(unit.value!!)
    val upPrice: Int get() = if (_bid.value == null) 1000 else _bid.value!!.plus(unit.value!!)

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
            runStomp(context)
            getAuctionInfo(landmarkId)
        }
    }

    fun increaseBid() {
        _bid.value = _bid.value!!.plus(unit.value!!)
    }

    fun decreaseBid() {
        _bid.value = _bid.value!!.minus(unit.value!!)
    }

    private fun getAuctionHistory(landmarkId: Long) {
        viewModelScope.launch {
            try {
                val result = auctionRepository.getAuctionHistory(landmarkId).toTypedArray()
                val modifiedList = mutableListOf<Int>()
                modifiedList.addAll(result)
                modifiedList.add(_bid.value!!)
                _auctionHistory.value = modifiedList.toTypedArray()
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

    fun createBidding(onComplete: (Boolean) -> Unit) {
        val requestBody = AuctionBiddingRequest(landmarkId, _bid.value!!)
        viewModelScope.launch {
            val success = withContext(viewModelScope.coroutineContext) {
                auctionRepository.createBidding(requestBody)
            }

            onComplete(success)
        }
    }

    fun updateBidding(onComplete: (Boolean) -> Unit) {
        val requestBody = AuctionBiddingRequest(landmarkId, _bid.value!!)
        viewModelScope.launch {
            val success = withContext(viewModelScope.coroutineContext) {
                auctionRepository.updateBidding(requestBody)
            }

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

    private lateinit var stompClient: StompClient

    @SuppressLint("CheckResult")
    private fun runStomp(context: Context) {
        val endpointUrl = "wss://k8a403.p.ssafy.io/socket"

        val prefUtil = PreferenceUtil(context)
        val accessToken = prefUtil.getString("accessToken")
        val tokenInfo = "Bearer $accessToken"

        val headers: MutableMap<String, String> = HashMap()
        headers["Authorization"] = tokenInfo
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, endpointUrl, headers)

        stompClient.lifecycle().subscribe { lifecycleEvent ->
            when (lifecycleEvent.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.i("OPEND", "!!")

                    val data = JSONObject()
                    data.put("price", 0)
                    data.put("landmarkId", landmarkId)

                    stompClient.send("/app/bid", data.toString()).subscribe(
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
            val getLandmarkId = body.getLong("landmarkId")
            val price = body.getInt("price")
            if (getLandmarkId == landmarkId) {
                Log.i("Received Message", "price: $price")
                _currentHighestBid.postValue(price)
                val divideValue = price.div(10)
                val stringValue = divideValue.toString()
                val lengthValue = stringValue.length - 1
                _unit.postValue(10.0.pow(lengthValue.toDouble()).toInt())
                _bid.postValue(price.plus(10.0.pow(lengthValue.toDouble()).toInt()))
                getAuctionHistory(landmarkId)
            }
        }, { error ->
            Log.e("Subscription Error", "Failed to subscribe to topic", error)
        })
    }

    fun viewModelOnCleared() {
        stompClient.disconnect()
        _currentHighestBid.value = 0
        _bid.value = 0
        _unit.value = 0
    }
}