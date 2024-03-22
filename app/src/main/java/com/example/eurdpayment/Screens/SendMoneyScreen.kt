package com.example.eurdpayment.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.eurdpayment.Algorand.assetList
import com.example.eurdpayment.Components.Input.DropdownList
import com.example.eurdpayment.Components.Input.NumberInput
import com.example.eurdpayment.Components.Input.ReceiverInput
import com.example.eurdpayment.Navigation.EurdPaymentScreen

@Composable
fun SendMoneyScreen(navController: NavController) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), // Fill the maximum height to allow vertical centering
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center the contents vertically
    ){
        val itemList = assetList.map { it.name }
        var selectedAsset by remember { mutableStateOf(assetList[0])}
        var selectedNumber by remember {
            mutableStateOf(0f)
        }

        var buttonModifier = Modifier.width(100.dp)

        DropdownList(itemList = itemList, modifier = buttonModifier, onItemClick = {selectedAsset = assetList.first{asset -> asset.name == it}})

        NumberInput(onNumberSelected = {selectedNumber = it}, asset = selectedAsset)

        ReceiverInput()

        Button(onClick = {
            navController.navigate("${EurdPaymentScreen.PaymentScreen.route}/${selectedAsset.name}/${selectedNumber}") {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
            }
        }) {
            Text(text = "pay")
        }
    }
}