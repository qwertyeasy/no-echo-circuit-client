package com.qwertyeasy.no_echo_circuit_client.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwertyeasy.no_echo_circuit_client.ui.theme.InterBlack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.TronicaFont

@Composable
fun TitleButton(text: String, modifier: Modifier,
                contentColor: Color, containerColor: Color, onClick: () -> Unit){

    CommonButton(text, modifier, contentColor, containerColor, onClick,
        InterBlack, 73.sp, FontWeight.Black)
}

@Composable
fun PixelTextButton(text: String, modifier: Modifier,
                    contentColor: Color, containerColor: Color, onClick: () -> Unit){

    CommonButton(text, modifier, contentColor, containerColor, onClick,
        TronicaFont, 50.sp)
}

@Composable
fun SmallPixelTextButton(text: String, modifier: Modifier,
                    contentColor: Color, containerColor: Color, onClick: () -> Unit){

    CommonButton(text, modifier, contentColor, containerColor, onClick,
        TronicaFont, 40.sp)
}

@Composable
fun CommonButton(text: String, modifier: Modifier,
                 contentColor: Color, containerColor: Color, onClick: () -> Unit,
                 fontFamily: FontFamily, fontSize: TextUnit, fontWeight: FontWeight? = null){
    Button(
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = contentColor,
            containerColor = containerColor
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        modifier = modifier.fillMaxSize()
    ) {
        Text(text, fontFamily = fontFamily, fontSize = fontSize, fontWeight = fontWeight)
    }
}

@Composable
fun SquareButton(modifier: Modifier, iconId: Int, backColor: Color, frontColor: Color,
                 iconSize: Dp, onClick: () -> Unit, onLongClick: () -> Unit){
    Box(modifier = modifier
        .fillMaxSize()
        .background(backColor)
        .combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        ),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = iconId),
            modifier = Modifier.size(iconSize),
            contentDescription = "SquareIconButton",
            colorFilter = ColorFilter.tint(frontColor)
        )
    }
}