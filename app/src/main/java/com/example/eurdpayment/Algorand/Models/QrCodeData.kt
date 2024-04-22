package com.example.eurdpayment.Algorand.Models

import kotlinx.serialization.Serializable

@Serializable
data class QrCodeData(val receivingAddress: String, val assetId: Long, val amount: Float){}