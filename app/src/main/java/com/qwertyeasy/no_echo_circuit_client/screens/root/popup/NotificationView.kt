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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.qwertyeasy.no_echo_circuit_client.components.DismissSpacer
import com.qwertyeasy.no_echo_circuit_client.components.SmallPixelTextButton
import com.qwertyeasy.no_echo_circuit_client.components.prepareFatBorder
import com.qwertyeasy.no_echo_circuit_client.data.NotificationData
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.InterBlack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

@Composable
fun NotificationPopup(rootViewModel: RootViewModel, currentNotification: NotificationData){
    val descriptSize = calcDescriptSize(currentNotification.description)
    val columnHeight = 300.dp + descriptSize

    Column(Modifier.zIndex(1f).fillMaxSize()) {
        DismissSpacer(Modifier.weight(0.7f), { rootViewModel.onNotifyDismiss() })
        Column(Modifier.height(columnHeight)
            .fillMaxSize().background(NeonPurple)
        ) {
            Spacer(Modifier.weight(0.08f))
            TextBlock(Modifier.height(descriptSize), currentNotification)
            Spacer(Modifier.weight(0.04f))
            ButtonsBlock(Modifier.weight(0.25f), rootViewModel)
            Spacer(Modifier.weight(0.08f))
        }
        DismissSpacer(Modifier.height(100.dp), { rootViewModel.onNotifyDismiss() })
    }
}

private fun calcDescriptSize(description: String?): Dp {
    if(description == null) return 0.dp
    return (description.length / 20 * 40).dp
}

@Composable
fun TextBlock(modifier: Modifier, currentNotification: NotificationData){
    Column(modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Your contact was saved by", fontSize = 30.sp)
        Text(currentNotification.userNickname.uppercase(), fontSize = 45.sp, fontFamily = InterBlack)
        currentNotification.description?.let { Text(it, fontSize = 30.sp) }
    }
}

@Composable
fun ButtonsBlock(modifier: Modifier, rootViewModel: RootViewModel){
    Row(modifier) {
        Spacer(Modifier.weight(0.05f))
        SmallPixelTextButton("add", Modifier.weight(0.4f).border(prepareFatBorder(BlackBack)),
            NeonPurple, BlackBack, { rootViewModel.onAddButtonClicked() })
        Spacer(Modifier.weight(0.02f))
        SmallPixelTextButton("ignore", Modifier.weight(0.6f).border(prepareFatBorder(BlackBack)),
            BlackBack,NeonPurple, { rootViewModel.checkNextNotification() })
        Spacer(Modifier.weight(0.05f))
    }
}