package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.UserInfo
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface AuctionApi {
    @GET("/api/auctions/{landmarkId}")
    suspend fun getAuctionInfo(@Path("landmarkId") landmarkId: Long): Flow<UserInfo>
}