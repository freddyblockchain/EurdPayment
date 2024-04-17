package com.example.eurdpayment.Screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.eurdpayment.Algorand.Models.TransactionInfo
import com.example.eurdpayment.Algorand.dummyAccount
import com.example.eurdpayment.Algorand.getTransactionsFromAddress
import androidx.compose.foundation.lazy.items
import com.example.eurdpayment.Components.HistoryView

@Composable
fun HistoryScreen(navController: NavController) {
    val transactionInfos = remember { mutableStateListOf<TransactionInfo>() }

    LaunchedEffect(Unit) {
       transactionInfos.addAll(getTransactionsFromAddress(dummyAccount.address.encodeAsString()))
    }

    Text("History screen")
    LazyColumn {
        items(transactionInfos) { txInfo ->
            HistoryView(txInfo)
        }
    }

}