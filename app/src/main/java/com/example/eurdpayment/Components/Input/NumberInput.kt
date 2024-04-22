package com.example.eurdpayment.Components.Input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.eurdpayment.Algorand.Models.Asset
import com.example.eurdpayment.Algorand.getAssetCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun NumberInput(onNumberSelected: (Float) -> Unit, asset: Asset, setIsError: (Boolean) -> Unit) {
    var errorText by remember { mutableStateOf("") }
    var number = remember { mutableStateOf("") }
    var maxAmount by remember { mutableFloatStateOf(0f) }
    var showError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(asset.name) {
        val maxValue = getAssetCount(asset)
        maxAmount = maxValue
    }
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = number.value,
            onValueChange = { newValue ->
                if (newValue.matches(("^\\d*\\.?\\d{0,${asset.decimals}}$".toRegex()))) {
                    number.value = newValue
                    if(newValue.isEmpty() || newValue.toFloat() == 0f){
                        showError = true
                        setIsError(true)
                        errorText = "Amount is 0"
                    } else{

                        if(newValue.toFloat() > maxAmount){
                            showError = true
                            setIsError(true)
                            errorText = "Amount is higher than max amount"
                        }
                        else {
                            onNumberSelected(number.value.toFloat())
                            showError = false
                            setIsError(false)
                        }
                    }

                }
            },
            label = { Text("Enter amount") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus() // Hide the keyboard when Done is clicked
                },
            ),
            isError = showError
        )
        Text(text = "Max", textDecoration = TextDecoration.Underline, modifier = Modifier
            .padding(start = 10.dp)
            .clickable {
                number.value = maxAmount.toString()
                onNumberSelected(maxAmount)
            })
    }
    if (showError) {
        Text(
            text = errorText,
            color = androidx.compose.ui.graphics.Color.Red
        )
    }
}