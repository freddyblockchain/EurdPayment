package com.example.eurdpayment.Components.Input

import androidx.compose.foundation.layout.Column
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.eurdpayment.Algorand.Models.Asset
import com.example.eurdpayment.Algorand.getAanNames
import com.example.eurdpayment.Utils.SearchItemGrid


fun CommonPrefixStrings(allStrings: List<String>, stringToCompare: String): List<String> {
    val suggestedIngredientsFound = allStrings.filter {
        val trimmedString = stringToCompare.trim()
        val commonPrefix = it.commonPrefixWith(trimmedString)
        commonPrefix.length == trimmedString.length && commonPrefix.isNotEmpty()
    }
    return suggestedIngredientsFound
}

@Composable
fun ReceiverInput() {
    var inputName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var listData by remember { mutableStateOf<List<String>>(listOf()) }
    val suggestedNames = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        listData = getAanNames()
        val something = 1;
    }
    val inputChanged = { input: String ->
        if (input.isNotEmpty()) {
            val commonStrings = CommonPrefixStrings(listData, input)

            if (commonStrings.isNotEmpty()) {
                suggestedNames.clear()
                suggestedNames.addAll(commonStrings)
            } else {
                suggestedNames.clear()
            }
        }
        inputName = input
    }
    OutlinedTextField(
        value = inputName ,
        onValueChange = { newValue ->
            inputChanged(newValue)
            inputName = newValue
        },
        label = { Text("Enter Receiver") },
        isError = showError
    )

    Column() {
        if (inputName.isNotEmpty() && suggestedNames.isNotEmpty()) {
            SearchItemGrid(
                itemTexts = suggestedNames,
                onItemClick = { inputName = suggestedNames[it] })
        }
    }

    if (showError) {
        Text(
            text = "Name cannot be empty",
            color = Color.Red
        )
    }
    // Opti
}