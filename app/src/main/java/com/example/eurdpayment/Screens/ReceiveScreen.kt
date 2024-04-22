package com.example.eurdpayment.Screens

import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.eurdpayment.Navigation.EurdPaymentScreen
import com.example.eurdpayment.Utils.QRCodeImage
import com.example.eurdpayment.Utils.generateQRCode

@Composable
fun ReceiveScreen(navController: NavController) {
    Text("Receive screen")
    
    Button(onClick = {
        navController.navigate("${EurdPaymentScreen.QrCodeScreen.route}/${"USDC"}/${10.2}") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
        }
    }) {

    }
}