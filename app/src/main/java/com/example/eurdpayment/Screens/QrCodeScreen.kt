package com.example.eurdpayment.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.eurdpayment.Algorand.Models.Asset
import com.example.eurdpayment.Algorand.Models.QrCodeData
import com.example.eurdpayment.Algorand.dummyAccount
import com.example.eurdpayment.Utils.QRCodeImage
import com.example.eurdpayment.Utils.generateQRCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun QrCodeScreen(asset: Asset, amount:Float, navController: NavController) {
    val qrData = QrCodeData(dummyAccount.address.encodeAsString(), asset.id, amount)
    val qrCodeBitmap = generateQRCode(Json.encodeToString(qrData), 1024) // Adjust size as needed
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().fillMaxHeight(), verticalArrangement = Arrangement.Center) {
        Text("Send $amount ${asset.name} to me")
        QRCodeImage(qrCodeBitmap)
    }
}