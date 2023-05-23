package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.myAuction.LandmarkInfo
import com.shoebill.maru.model.data.myAuction.MyAuction
import com.shoebill.maru.model.interfaces.MyBiddingApi
import retrofit2.Response
import javax.inject.Inject

class MyBiddingRepository @Inject constructor(private val myBiddingApi: MyBiddingApi) {

    suspend fun getMyBiddings(lastOffset: Long?): Response<List<MyAuction>> =
        myBiddingApi.getMyBiddings(lastOffset)

    suspend fun getMyNonBiddings(lastOffset: Long): Response<List<LandmarkInfo>> =
        myBiddingApi.getMyNonBiddings(lastOffset)
}