package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.myAuction.LandmarkInfo
import com.shoebill.maru.model.data.myAuction.MyAuction
import com.shoebill.maru.model.interfaces.MyBiddingApi
import javax.inject.Inject

class MyBiddingRepository @Inject constructor(private val myBiddingApi: MyBiddingApi) {

    suspend fun getMyBiddings(lastOffset: Long?): List<MyAuction> =
        myBiddingApi.getMyBiddings(lastOffset)

    suspend fun getMyNonBiddings(lastOffset: Long): List<LandmarkInfo> =
        myBiddingApi.getMyNonBiddings(lastOffset)
}