package com.example.eurdpayment.Components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.eurdpayment.Algorand.Models.TransactionInfo
import com.example.eurdpayment.Algorand.dummyAccount
import com.example.eurdpayment.Algorand.getAanName

@Composable
fun HistoryView(transactionInfo: TransactionInfo) {
    val isReceiver = transactionInfo.receiver == dummyAccount.address.encodeAsString()
    var senderOrReceiver by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit) {
        senderOrReceiver = if(isReceiver){
            getAanName(transactionInfo.sender)} else {getAanName(transactionInfo.receiver)}
    }

    val sentOrReceived = if(isReceiver){"received"} else {"sent"}
    val sentOrReceived2 = if(isReceiver){"from"} else {"to"}
    val sentText = "You $sentOrReceived ${transactionInfo.amount} ${transactionInfo.asset.name} $sentOrReceived2 $senderOrReceiver"
    
    Text(text = sentText)
}