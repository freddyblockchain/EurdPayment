package com.example.eurdpayment.Components.Input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.eurdpayment.Algorand.Models.Asset
import com.example.eurdpayment.Algorand.getAanAccountAdress
import com.example.eurdpayment.Algorand.getAanNames
import com.example.eurdpayment.Utils.SearchItemGrid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun CommonPrefixStrings(allStrings: List<String>, stringToCompare: String): List<String> {
    val suggestedIngredientsFound = allStrings.filter {
        val trimmedString = stringToCompare.trim()
        val commonPrefix = it.commonPrefixWith(trimmedString)
        commonPrefix.length == trimmedString.length && commonPrefix.isNotEmpty()
    }
    return suggestedIngredientsFound
}

@Composable
fun ReceiverInput(onReceiverChanged: (String) -> Unit, setIsError: (Boolean) -> Unit) {
    var inputName by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    var isError by remember {
        mutableStateOf(true)
    }
    var listData by remember { mutableStateOf<List<String>>(listOf()) }
    val suggestedNames = remember { mutableStateListOf<String>() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        listData = getAanNames()
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
        } else {
            isError = true
            errorText = "Receiver cannot be empty"
        }
        inputName = input
        println(inputName.length)
        if(inputName.length != 58){
            errorText = "Receiver must be valid algorand address"
            setIsError(true)
            isError = true
        } else {
            onReceiverChanged(inputName)
            isError = false
            setIsError(isError)
        }
    }
    OutlinedTextField(
        value = inputName,
        onValueChange = { newValue ->
            inputChanged(newValue)
            inputName = newValue
        },
        label = { Text("Enter Receiver") },
        isError = isError,
        modifier = Modifier.widthIn(max = 280.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide() // Hide the keyboard when "Done" is pressed
            }
        )

    )

    Column() {
        if (inputName.isNotEmpty() && suggestedNames.isNotEmpty()) {
            SearchItemGrid(
                itemTexts = suggestedNames,
                onItemClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val correspondingAddress = getAanAccountAdress(suggestedNames[it])
                        inputName = correspondingAddress
                        inputChanged(inputName)
                    }
                })
        }

        if (isError) {
            Text(
                text = errorText,
                color = Color.Red
            )
        }
        // Opti
    }
}