package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.myBiddings.LandmarkInfo
import com.shoebill.maru.model.data.myBiddings.MyBidding
import retrofit2.http.GET
import retrofit2.http.Query

interface MyBiddingApi {

    @GET("api/auctions/my/biddings")
    suspend fun getMyBiddings(
        @Query("lastOffset") lastOffset: Long?,
        @Query("size") size: Int = 20
    ): List<MyBidding>

    @GET("api/auctions/my/non-biddings")
    suspend fun getMyNonBiddings(
        @Query("lastOffset") lastOffset: Long,
        @Query("size") size: Int = 20
    ): List<LandmarkInfo>

}