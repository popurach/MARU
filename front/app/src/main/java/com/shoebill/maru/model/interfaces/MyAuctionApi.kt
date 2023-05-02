package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.notice.Notice
import retrofit2.http.GET
import retrofit2.http.Query

interface MyAuctionApi {
    @GET("api/auctions/my")
    suspend fun getMyAuction(
        @Query("bidding") bidding: Boolean,
        @Query("lastOffset") lastOffset: Long = 15,
        @Query("size") size: Int = 20
    ): List<Notice>
}