package com.shoebill.maru.model.repository

import com.shoebill.maru.model.interfaces.AuctionApi
import javax.inject.Inject

class AuctionRepository @Inject constructor(private val auctionApi: AuctionApi) {
    suspend fun getAuctionInfo(landmarkId: Long) = auctionApi.getAuctionInfo(landmarkId)
}