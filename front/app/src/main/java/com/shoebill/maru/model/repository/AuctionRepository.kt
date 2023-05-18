package com.shoebill.maru.model.repository

import com.shoebill.maru.model.data.AuctionBiddingRequest
import com.shoebill.maru.model.data.AuctionInfo
import com.shoebill.maru.model.interfaces.AuctionApi
import javax.inject.Inject

class AuctionRepository @Inject constructor(private val auctionApi: AuctionApi) {
    suspend fun getAuctionHistory(landmarkId: Long): List<Int> {
        val response = auctionApi.getAuctionHistory(landmarkId)
        if (response.isSuccessful) {
            val body = response.body()
            return body ?: listOf()
        } else {
            throw Exception("getAuctionHistory fail: ${response.code()}")
        }
    }

    suspend fun getAuctionInfo(landmarkId: Long): AuctionInfo {
        val response = auctionApi.getAuctionInfo(landmarkId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            } else {
                throw Exception("AuctionInfo is null")
            }
        } else {
            throw Exception("getAuctionInfo fail: ${response.code()}")
        }
    }

    suspend fun createBidding(requestBody: AuctionBiddingRequest): Boolean {
        val response = auctionApi.createBidding(requestBody)
        return response.isSuccessful
    }

    suspend fun deleteBidding(auctionLogId: Long): Boolean {
        val response = auctionApi.deleteBidding(auctionLogId)
        if (response.isSuccessful) {
            return response.isSuccessful
        } else {
            throw Exception("deleteBidding fail: ${response.code()}")
        }
    }
}