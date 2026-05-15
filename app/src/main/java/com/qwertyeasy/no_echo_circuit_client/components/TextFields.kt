package com.qwertyeasy.no_echo_circuit_client.components

import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun TableTextField(nickname: String, containerColor: Color,
                   textColor: Color, onValueChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = nickname,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.colors().copy(
            cursorColor = textColor,
            focusedTextColor = textColor,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor = Color.Transparent
        ),
        visualTransformation = VisualTransformation { text ->
            TransformedText(
                AnnotatedString(text.text.uppercase()),
                OffsetMapping.Identity
            )
        }
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}