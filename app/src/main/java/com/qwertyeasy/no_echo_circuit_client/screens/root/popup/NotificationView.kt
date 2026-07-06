package com.qwertyeasy.no_echo_circuit_client.screens.root.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.qwertyeasy.no_echo_circuit_client.components.DismissSpacer
import com.qwertyeasy.no_echo_circuit_client.components.SmallPixelTextButton
import com.qwertyeasy.no_echo_circuit_client.components.prepareFatBorder
import com.qwertyeasy.no_echo_circuit_client.data.NotificationData
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.InterBlack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

@Composable
fun NotificationPopup(
    onNotifyDismiss: () -> Unit, onAddButtonClicked: () -> Unit,
    checkNextNotification: () -> Unit, currentNotification: NotificationData
){

    Column(Modifier.zIndex(1f).fillMaxSize()) {
        DismissSpacer(Modifier.weight(0.7f), onNotifyDismiss)
        Row(Modifier.fillMaxWidth().background(NeonPurple)) {
            Spacer(Modifier.weight(0.05f))
            Column(Modifier.weight(0.9f)) {
                Spacer(Modifier.height(30.dp))
                TextBlock(currentNotification)
                Spacer(Modifier.height(15.dp))
                ButtonsBlock(Modifier.height(100.dp), onAddButtonClicked, checkNextNotification)
                Spacer(Modifier.height(30.dp))
            }
            Spacer(Modifier.weight(0.05f))
        }
        DismissSpacer(Modifier.height(100.dp), onNotifyDismiss)
    }
}

@Composable
fun TextBlock(currentNotification: NotificationData){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Your contact was saved by", fontSize = 30.sp)
        Text(currentNotification.userNickname.uppercase(), fontSize = 45.sp, fontFamily = InterBlack)
        currentNotification.description?.let { Text(it, fontSize = 20.sp) }
    }
}

@Composable
fun ButtonsBlock(modifier: Modifier, onAddButtonClicked: () -> Unit, checkNextNotification: () -> Unit){
    Row(modifier) {
        SmallPixelTextButton("add", Modifier.weight(0.4f).border(prepareFatBorder(BlackBack)),
            NeonPurple, BlackBack, onAddButtonClicked)
        Spacer(Modifier.weight(0.02f))
        SmallPixelTextButton("ignore", Modifier.weight(0.6f).border(prepareFatBorder(BlackBack)),
            BlackBack,NeonPurple, checkNextNotification)
    }
}