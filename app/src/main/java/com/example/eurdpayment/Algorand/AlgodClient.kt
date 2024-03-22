package com.example.eurdpayment.Algorand

import com.algorand.algosdk.account.Account
import com.algorand.algosdk.v2.client.common.AlgodClient


val algod = AlgodClient("https://testnet-api.algonode.cloud", 443, "")

val receivingAccount = "MVP7P2QM2J2R45ZPHBKM26J4PZTYNMLK57XUFZWU4LF5RMYJJ3UZHASWWY"

val dummyAccount = Account("" +
        "glance fame avocado team tobacco spoon actress author situate swarm embark check design reform radio alien bachelor matter best diesel whip select idle absorb film")

val AanAppId: Long = 614314262