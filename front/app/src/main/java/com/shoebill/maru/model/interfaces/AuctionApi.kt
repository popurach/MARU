package com.shoebill.maru.model.interfaces

import com.shoebill.maru.model.data.AuctionBiddingRequest
import com.shoebill.maru.model.data.AuctionInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuctionApi {
    @GET("api/auctions/landmarks/{landmarkId}")
    suspend fun getAuctionHistory(@Path("landmarkId") landmarkId: Long): Response<List<Int>>

    @GET("api/auctions/{landmarkId}/details")
    suspend fun getAuctionInfo(@Path("landmarkId") landmarkId: Long): Response<AuctionInfo>

    @GET("api/auctions/landmarks/price/{landmarkId}")
    suspend fun getBiddingPrice(@Path("landmarkId") landmarkId: Long): Response<Int>

    @POST("api/auctions/bidding")
    suspend fun createBidding(@Body requestBody: AuctionBiddingRequest): Response<Unit>

    @PUT("api/auctions/bidding")
    suspend fun updateBidding(@Body requestBody: AuctionBiddingRequest): Response<Unit>

    @DELETE("api/auctions/bidding/{auctionLogId}")
    suspend fun deleteBidding(@Path("auctionLogId") auctionLogId: Long): Response<Unit>

}