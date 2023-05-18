package com.shoebill.maru.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.shoebill.maru.BuildConfig
import com.shoebill.maru.model.data.AuctionBiddingRequest
import com.shoebill.maru.model.data.AuctionInfo
import com.shoebill.maru.model.repository.AuctionRepository
import com.shoebill.maru.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
) : ViewModel() {
    private val _currentHighestBid = MutableLiveData(0)
    val currentHighestBid: LiveData<Int> = _currentHighestBid

    private val _bid = MutableLiveData<Int>(0)
    val bid: LiveData<Int> = _bid

    private val _unit = MutableLiveData<Int>(0)
    val unit: LiveData<Int> = _unit

    val downPrice: Int get() = if (_bid.value == null) 1000 else _bid.value!!.minus(unit.value!!)
    val upPrice: Int get() = if (_bid.value == null) 1000 else _bid.value!!.plus(unit.value!!)

    private val _auctionHistory = MutableLiveData<Array<Int>>()
    val auctionHistory: LiveData<Array<Int>> = _auctionHistory

    private val _auctionInfo = MutableLiveData<AuctionInfo>()
    val auctionInfo: LiveData<AuctionInfo> = _auctionInfo

    private var landmarkId: Long = 2

    private val _isConnected = MutableLiveData(false)
    val isConnected get() = _isConnected

    private var isPop: Boolean = false

    fun initLandmarkId(value: Long) {
        landmarkId = value
        viewModelScope.launch {
            getAuctionInfo(landmarkId)
            getAuctionHistory(landmarkId)
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
                val result = withContext(Dispatchers.IO) {
                    auctionRepository.getAuctionHistory(landmarkId).toTypedArray()
                }
                _auctionHistory.value = result
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
            } catch (e: Exception) {
                Log.d(TAG, "getAuctionInfo: $e")
            }
        }
    }

    fun createBidding(onComplete: (Boolean) -> Unit) {
        val requestBody = AuctionBiddingRequest(landmarkId, _bid.value!!)
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                auctionRepository.createBidding(requestBody)
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

    lateinit var stompClient: StompClient

    @SuppressLint("CheckResult")
    fun runStomp(context: Context, navController: NavHostController) {
        viewModelScope.launch {
            _isConnected.value = true
            val prefUtil = PreferenceUtil(context)
            val accessToken = prefUtil.getString("accessToken")
            val tokenInfo = "Bearer $accessToken"

            val headers: MutableMap<String, String> = HashMap()
            headers["Authorization"] = tokenInfo
            stompClient =
                Stomp.over(Stomp.ConnectionProvider.OKHTTP, BuildConfig.END_POINT_URL, headers)
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
                        _isConnected.postValue(false)
                    }

                    LifecycleEvent.Type.ERROR -> {
                        Log.i("CONNECT ERROR", lifecycleEvent.exception.toString())
                        stompClient.disconnect()
                        if (!isPop) {
                            isPop = true
                            viewModelScope.launch {
                                _isConnected.value = false
                                navController.navigateUp()
                            }
                        }
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
                    _currentHighestBid.postValue(price)
                    val history = _auctionHistory.value?.toMutableList() ?: mutableListOf()
                    if (history.size > 1) history.removeAt(history.size - 1)
                    history.add(price)
                    _auctionHistory.postValue(history.toTypedArray())
                    val divideValue = price.div(10)
                    val stringValue = divideValue.toString()
                    val lengthValue = stringValue.length - 1
                    _unit.postValue(10.0.pow(lengthValue.toDouble()).toInt())
                    _bid.postValue(price.plus(10.0.pow(lengthValue.toDouble()).toInt()))
                }
            }, { error ->
                Log.e("Subscription Error", "Failed to subscribe to topic", error)
            })
        }

    }

    fun viewModelOnCleared() {
        stompClient.disconnect()
        _currentHighestBid.value = 0
        _bid.value = 0
        _unit.value = 0
        isPop = false
    }
}