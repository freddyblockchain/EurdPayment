package com.example.eurdpayment.Components

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun SlideComponent(onConfirmed: () -> Unit) {

    val sliderWidth = 300.dp // Define the width of your slider
    val buttonWidth = 60.dp // Define the width of the sliding button

    val sliderWidthPx = with(LocalDensity.current) { sliderWidth.toPx() }
    val buttonWidthPx = with(LocalDensity.current) { buttonWidth.toPx() }
    val maxDragDistance = sliderWidthPx - buttonWidthPx // Maximum distance the sli

    var dragEndedAndOverLimit = false

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .width(sliderWidth)
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.CenterStart
    ) {
        Text("Slide to pay", modifier = Modifier.align(Alignment.Center))
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(buttonWidth, 60.dp)
                .background(Color.Green)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            if (offsetX > maxDragDistance / 2) {
                                dragEndedAndOverLimit = true;
                                coroutineScope.launch {
                                    animate(
                                        initialValue = offsetX,
                                        targetValue = maxDragDistance,
                                        animationSpec = tween(durationMillis = 300) // Adjust the duration according to your needs
                                    ) { value, _ ->
                                        offsetX = value
                                    }
                                    onConfirmed()
                                }
                            } else{
                                offsetX = 0f
                            }
                        },
                        onDrag = { change, dragAmount ->
                            if(!dragEndedAndOverLimit){
                                change.consume()
                                val proposedOffsetX = offsetX + dragAmount.x
                                offsetX = proposedOffsetX.coerceIn(0f, maxDragDistance)
                            }
                        }
                    )
                }
        )
    }
}