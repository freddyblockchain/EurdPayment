package com.example.eurdpayment.Utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchItemGrid(itemTexts: List<String>, onItemClick: (Int) -> Unit) {
    for (i in 0..1) {
        Row{
            for (j in 0..1) {
                val index = i * 2 + j
                SearchItem(itemTexts = itemTexts, index = index, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun SearchItem(itemTexts: List<String>, index: Int, onItemClick: (Int) -> Unit) {
    if (index in itemTexts.indices) {
        Text(
            text = itemTexts[index],
            fontSize = 18.sp,
            color = Color.Black,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .clickable(onClick = { onItemClick(index) })
                .padding(end = 16.dp)
        )
    }
}