package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.myBiddings.LandmarkInfo
import com.shoebill.maru.model.data.myBiddings.MyBidding
import com.shoebill.maru.model.interfaces.MyBiddingApi
import javax.inject.Inject

class MyBiddingRepository @Inject constructor(private val myBiddingApi: MyBiddingApi) {

    suspend fun getMyBiddings(lastOffset: Long?): List<MyBidding> =
        myBiddingApi.getMyBiddings(lastOffset)

    suspend fun getMyNonBiddings(lastOffset: Long): List<LandmarkInfo> =
        myBiddingApi.getMyNonBiddings(lastOffset)
}