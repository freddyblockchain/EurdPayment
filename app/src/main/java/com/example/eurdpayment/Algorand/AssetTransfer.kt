package com.example.eurdpayment.Algorand

import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.BoxesResponse
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.example.eurdpayment.Algorand.Models.Asset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.charset.StandardCharsets

fun convertFloatToAssetInteger(asset: Asset, usdcAmount: Float): Long {
    // Convert the float amount to a BigDecimal
    val amount = BigDecimal(usdcAmount.toString())
    // Define the conversion factor (10^DIVISIBILITY) as a BigDecimal
    val conversionFactor: BigDecimal = BigDecimal.valueOf(10).pow(asset.decimals)
    // Multiply the amount by the conversion factor
    val convertedAmount: BigDecimal = amount.multiply(conversionFactor)
    // Convert the result to a long value for the integer representation
    return convertedAmount.toLong()
}

fun convertBigIntToFloat(asset: Asset, bigIntAmount: BigInteger): Float{
    val conversionFactor: BigDecimal = BigDecimal.valueOf(10).pow(asset.decimals)
    // Convert the integer amount to a BigDecimal
    val amount = BigDecimal(bigIntAmount)
    // Divide the amount by the conversion factor
    val convertedAmount: BigDecimal = amount.divide(conversionFactor)
    // Convert the result to a float value
    return convertedAmount.toFloat()
}
fun AssetTransfer(asset: Asset, amount: Float): Boolean {
    val rsp: Response<TransactionParametersResponse> = algod.TransactionParams().execute()
    val sp: TransactionParametersResponse = rsp.body()

    val assetAmountAsLong = convertFloatToAssetInteger(asset, amount)

    val xferTxn: Transaction = Transaction.AssetTransferTransactionBuilder().suggestedParams(sp)
        .sender(dummyAccount.address)
        .assetReceiver(receivingAccount)
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