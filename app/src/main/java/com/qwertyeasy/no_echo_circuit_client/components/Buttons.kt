package com.qwertyeasy.no_echo_circuit_client.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwertyeasy.no_echo_circuit_client.ui.theme.InterBlack

@Composable
fun TitleButton(text: String, modifier: Modifier,
                contentColor: Color, containerColor: Color, onClick: () -> Unit){
    Button(
        onClick = onClick,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = contentColor,
            containerColor = containerColor
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text, fontFamily = InterBlack,
            fontSize = 73.sp, fontWeight = FontWeight.Black)
    }
}