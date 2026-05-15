package com.qwertyeasy.no_echo_circuit_client.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack

@Composable
fun DismissSpacer(modifier: Modifier, onDismiss: () -> Unit){
    Spacer(modifier.fillMaxWidth().background(BlackBack.copy(0.5f))
        .clickable(onClick = onDismiss))
}