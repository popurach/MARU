package com.shoebill.maru.model.repository

import android.util.Log
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

    suspend fun getBiddingPrice(landmarkId: Long): Int {
        val response = auctionApi.getBiddingPrice(landmarkId)
        if (response.isSuccessful) {
            val body = response.body()
            return body ?: 0
        } else {
            throw Exception("getBiddingPrice fail: ${response.code()}")
        }
    }

    suspend fun createBidding(requestBody: AuctionBiddingRequest): Boolean {
        val response = auctionApi.createBidding(requestBody)
        Log.d("AUCTION", "createBidding: $response")
        return response.isSuccessful
    }

    suspend fun updateBidding(requestBody: AuctionBiddingRequest): Boolean {
        val response = auctionApi.updateBidding(requestBody)
        Log.d("AUCTION", "updateBidding: $response")
        return response.isSuccessful
    }

    suspend fun deleteBidding(auctionLogId: Long): Boolean {
        val response = auctionApi.deleteBidding(auctionLogId)
        if (response.isSuccessful) {
            Log.d("AUCTION", "deleteBidding: $response")
            return response.isSuccessful
        } else {
            throw Exception("deleteBidding fail: ${response.code()}")
        }
    }
}