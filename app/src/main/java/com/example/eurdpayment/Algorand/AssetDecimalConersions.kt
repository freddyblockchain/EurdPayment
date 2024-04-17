package com.example.eurdpayment.Algorand

import com.example.eurdpayment.Algorand.Models.Asset
import java.math.BigDecimal
import java.math.BigInteger

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