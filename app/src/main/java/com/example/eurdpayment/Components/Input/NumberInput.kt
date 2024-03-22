package com.example.eurdpayment.Components.Input

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.eurdpayment.Algorand.Models.Asset
import com.example.eurdpayment.Algorand.getAssetCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun NumberInput(onNumberSelected: (Float) -> Unit, asset: Asset) {
    var number = remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = number.value,
            onValueChange = { newValue ->
                if (newValue.matches(("^\\d*\\.?\\d{0,${asset.decimals}}$".toRegex()))) {
                    number.value = newValue
                    if(newValue.length == 0){
                        onNumberSelected(0f);
                    } else{
                        onNumberSelected(number.value.toFloat())
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
                }
            ),
            isError = showError
        )
        Text(text = "Max", textDecoration = TextDecoration.Underline, modifier = Modifier.padding(start = 10.dp).clickable {
            CoroutineScope(Dispatchers.IO).launch{
                val maxValue = getAssetCount(asset)
                number.value = maxValue.toString()
                onNumberSelected(maxValue)
            }
        })
    }
    if (showError) {
        Text(
            text = "Name cannot be empty",
            color = androidx.compose.ui.graphics.Color.Red
        )
    }
    // Opti
}