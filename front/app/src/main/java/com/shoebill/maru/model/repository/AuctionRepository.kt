package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.AuctionBiddingRequest
import com.shoebill.maru.model.data.AuctionInfo
import com.shoebill.maru.model.interfaces.AuctionApi
import retrofit2.Response
import javax.inject.Inject

class AuctionRepository @Inject constructor(private val auctionApi: AuctionApi) {
    suspend fun getAuctionHistory(landmarkId: Long): Response<List<Int>> =
        auctionApi.getAuctionHistory(landmarkId)

    suspend fun getAuctionInfo(landmarkId: Long): Response<AuctionInfo> =
        auctionApi.getAuctionInfo(landmarkId)

    suspend fun createBidding(requestBody: AuctionBiddingRequest): Response<Unit> =
        auctionApi.createBidding(requestBody)

    suspend fun deleteBidding(auctionLogId: Long): Response<Unit> =
        auctionApi.deleteBidding(auctionLogId)
}