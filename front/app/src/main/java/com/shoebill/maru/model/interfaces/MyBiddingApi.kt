package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.myAuction.LandmarkInfo
import com.shoebill.maru.model.data.myAuction.MyAuction
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyBiddingApi {

    @GET("api/auctions/my/biddings")
    suspend fun getMyBiddings(
        @Query("lastOffset") lastOffset: Long?,
        @Query("size") size: Int = 20,
    ): Response<List<MyAuction>>

    @GET("api/auctions/my/non-biddings")
    suspend fun getMyNonBiddings(
        @Query("lastOffset") lastOffset: Long,
        @Query("size") size: Int = 20,
    ): Response<List<LandmarkInfo>>

}