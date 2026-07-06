package com.qwertyeasy.no_echo_circuit_client.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

//TODO: Нужен рефакторинг этих Composable
@Composable
fun TableTextField(string: String, containerColor: Color,
               textColor: Color, onValueChange: (String) -> Unit
){
    CommonTextField(
        string = string, containerColor = containerColor,
        textColor = textColor, onValueChange = onValueChange,
        isSingleLine = true,
        visualTransformation = VisualTransformation { text ->
            TransformedText(
                AnnotatedString(text.text.uppercase()),
                OffsetMapping.Identity
            )
        }
    )
}

@Composable
fun MultiLineTextField(string: String, containerColor: Color,
              textColor: Color, onValueChange: (String) -> Unit
){
    CommonTextField(
        string = string, containerColor = containerColor,
        textColor = textColor, onValueChange = onValueChange,
        isSingleLine = false
    )
}

@Composable
fun ExtendingTextField(string: String, containerColor: Color,
                       textColor: Color, onValueChange: (String) -> Unit
){
    CommonTextField(
        string = string, containerColor = containerColor,
        textColor = textColor, fontSize = 20.sp,
        onValueChange = onValueChange, isSingleLine = false
    )
}

@Composable
fun CommonTextField(string: String, containerColor: Color, isSingleLine: Boolean,
                    visualTransformation: VisualTransformation = VisualTransformation.None,
                    textColor: Color, fontSize: TextUnit = 30.sp, onValueChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        textStyle = LocalTextStyle.current.copy(fontSize = fontSize),
        value = string,
        maxLines = 6,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        onValueChange = onValueChange,
        singleLine = isSingleLine,
        colors = TextFieldDefaults.colors().copy(
            cursorColor = textColor,
            focusedTextColor = textColor,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        visualTransformation = visualTransformation
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}