package com.example.eurdpayment.Algorand.Models

import java.util.Date

data class TransactionInfo(val amount: Float, val asset: Asset, val sender: String, val receiver: String, val time: Date)
