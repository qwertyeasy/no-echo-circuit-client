package com.qwertyeasy.no_echo_circuit_client.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack

@Composable
fun DismissSpacer(modifier: Modifier, onDismiss: () -> Unit){
    Spacer(modifier
        .fillMaxWidth()
        .background(BlackBack.copy(0.5f))
        .clickable(onClick = onDismiss))
}

var lineThick = 3.8.dp

@Composable
fun HorizontalLine(color: Color){
    Spacer(Modifier
        .fillMaxWidth()
        .height(lineThick)
        .background(color)
    )
}

@Composable
fun VerticalLine(color: Color){
    Spacer(Modifier
        .fillMaxHeight()
        .width(lineThick)
        .background(color)
    )
}

fun prepareBorder(color: Color) = BorderStroke(lineThick, color)

fun prepareFatBorder(color: Color) = BorderStroke(lineThick*2, color)

fun Modifier.threeSidedBorder(
    color: Color
): Modifier{
    return this.then(Modifier.drawBehind {
        val widthPx = lineThick.toPx()
        val half = widthPx / 2f

        drawLine(color = color, start = Offset(half,0f),
            end = Offset(half, size.height),
            strokeWidth = widthPx
        )
        drawLine(color = color, start = Offset(size.width-half,0f),
            end = Offset(size.width-half, size.height),
            strokeWidth = widthPx
        )
        drawLine(color = color, start = Offset(0f,size.height),
            end = Offset(size.width, size.height),
            strokeWidth = widthPx
        )
    })
}