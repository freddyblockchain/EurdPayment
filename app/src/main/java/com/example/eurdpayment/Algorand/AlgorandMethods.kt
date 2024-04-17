package com.example.eurdpayment.Algorand

import android.os.Build
import androidx.annotation.RequiresApi
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.BoxesResponse
import com.algorand.algosdk.v2.client.model.Enums
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.example.eurdpayment.Algorand.Models.Asset
import com.example.eurdpayment.Algorand.Models.TransactionInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets
import java.util.Date
import java.util.Base64;

fun AssetTransfer(asset: Asset, amount: Float, assetReceiver: String): Boolean {
    val rsp: Response<TransactionParametersResponse> = algod.TransactionParams().execute()
    val sp: TransactionParametersResponse = rsp.body()

    val assetAmountAsLong = convertFloatToAssetInteger(asset, amount)

    val xferTxn: Transaction = Transaction.AssetTransferTransactionBuilder().suggestedParams(sp)
        .sender(dummyAccount.address)
        .assetReceiver(assetReceiver)
        .assetIndex(asset.id)
        .assetAmount(assetAmountAsLong)
        .build()

    val signedTransaction: SignedTransaction = dummyAccount.signTransaction(xferTxn)
    val signedTransactonAsBytes: ByteArray = Encoder.encodeToMsgPack(signedTransaction)

    // Send transaction
    // Send transaction
    val post = algod.RawTransaction().rawtxn(signedTransactonAsBytes).execute()
    if (!post.isSuccessful) {
        throw RuntimeException("Failed to post transaction")
    }

    // Wait for confirmation
    var done = false
    while (!done) {
        val txInfo = algod.PendingTransactionInformation(post.body().txId).execute()
        if (!txInfo.isSuccessful) {
            throw RuntimeException("Failed to check on tx progress")
        }
        Thread.sleep(100)
        if (txInfo.body().confirmedRound != null) {
            return true
        }
    }
    return false
}

suspend fun getAssetCount (asset: Asset): Float = withContext(Dispatchers.IO) {
    val information = algod.AccountAssetInformation(dummyAccount.address, asset.id.toLong()).execute()
    val amount = information.body().assetHolding.amount
    convertBigIntToFloat(asset, amount)
}

suspend fun getAanNames(): List<String> = withContext(Dispatchers.IO) {
    try {
        val boxesRequest = algod.GetApplicationBoxes(AanAppId)
        val boxesResponse: BoxesResponse = boxesRequest.execute().body() ?: return@withContext listOf()
        val boxes = boxesResponse.boxes
        val finalNameList = boxes?.mapNotNull { box ->
            val name = String(box.name, StandardCharsets.UTF_8)
            if (box.name.size < 32 && name.trim().length == name.length) name else null
        } ?: emptyList()
        return@withContext finalNameList
    } catch (e: Exception) {
        println(e.message)
        println("Names not found")
    }
    listOf()
}

suspend fun getAanAccountAdress(aanName: String): String = withContext(Dispatchers.IO){
    try{
        if(aanName.length >= 32){
            println("aanNameTooLong")
            return@withContext ""
        }
        val boxesRequest = algod.GetApplicationBoxByName(AanAppId)
        val box = boxesRequest.name("str:$aanName").execute()
        val boxValue = box.body()
        val address = Address(boxValue.value)
        val result = address.encodeAsString()
        return@withContext result
    } catch (err: Exception) {
        println(err.message)
        println("Names not found")
    }
    ""
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun getAanName(algorandAddress: String): String = withContext(Dispatchers.IO){
    try {
        val boxesRequest = algod.GetApplicationBoxByName(AanAppId)
        val box = boxesRequest.name("addr:$algorandAddress").execute()
        val boxValue = box.body()
        val decodedString = String(Base64.getDecoder().decode(boxValue.value()))
        return@withContext decodedString ?: algorandAddress
    } catch (err: Exception) {
        println(err.message)
        println("Names not found")
    }
    algorandAddress
}

suspend fun getTransactionsFromAddress(address: String): List<TransactionInfo> = withContext(Dispatchers.IO){
    val listOfTransactions = mutableListOf<TransactionInfo>()
    try{
        val minRound = 1L // example round number, adjust as necessary

        val maxRound: Long? = null // you can specify max round if needed


        val transactions = indexer.lookupAccountTransactions(Address(address))
            .execute()

        val body = transactions.body()
        body.transactions.forEach { transaction ->
            if(transaction.txType == Enums.TxType.AXFER){
                val assetTransaction = transaction.assetTransferTransaction
                val assetId = assetTransaction.assetId
                val correspondingAsset = assetList.firstOrNull { it.id == assetId }
                if(correspondingAsset != null){
                    val amount = convertBigIntToFloat(correspondingAsset, assetTransaction.amount)
                    val sender = transaction.sender
                    val receiver = assetTransaction.receiver

                    val roundTime = transaction.roundTime // Example timestamp in seconds

                    // Create a Date object from seconds
                    val date = Date(roundTime * 1000)
                    val transactionInfo: TransactionInfo = TransactionInfo(
                        amount = amount,
                        asset = correspondingAsset,
                        sender = sender,
                        receiver = receiver,
                        time = date
                    )
                    listOfTransactions.add(transactionInfo)
                }
            }
        }
    } catch (err: Exception) {
        println(err.message)
        println("Names not found")
    }
    listOfTransactions.toList()
}
