package com.qwertyeasy.no_echo_circuit_client.screens.root.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.qwertyeasy.no_echo_circuit_client.components.SmallPixelTextButton
import com.qwertyeasy.no_echo_circuit_client.components.prepareFatBorder
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

@Composable
fun FailedToConnectPopup(onRetryToConnect: () -> Unit, onExit: () -> Unit){
    Column(Modifier
        .zIndex(1f)
        .fillMaxSize()) {
        Spacer(Modifier.weight(0.7f))
        Column(Modifier
            .height(260.dp)
            .fillMaxSize()
            .background(NeonPurple)
        ) {
            Spacer(Modifier.weight(0.08f))
            Text("signal server isn't available")
            Spacer(Modifier.weight(0.04f))
            FailedToConnectButtonsBlock(Modifier.weight(0.2f), onRetryToConnect, onExit)
            Spacer(Modifier.weight(0.08f))
        }
        Spacer(Modifier.height(100.dp))
    }
}

@Composable
fun FailedToConnectButtonsBlock(modifier: Modifier, onRetryToConnect: () -> Unit, onExit: () -> Unit){
    Row(modifier) {
        Spacer(Modifier.weight(0.05f))
        SmallPixelTextButton("retry", Modifier
            .weight(0.55f)
            .border(prepareFatBorder(BlackBack)),
            NeonPurple, BlackBack, onRetryToConnect)
        Spacer(Modifier.weight(0.02f))
        SmallPixelTextButton("exit", Modifier
            .weight(0.45f)
            .border(prepareFatBorder(BlackBack)),
            BlackBack,NeonPurple, onExit)
        Spacer(Modifier.weight(0.05f))
    }
}