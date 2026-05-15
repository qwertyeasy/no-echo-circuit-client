package com.qwertyeasy.no_echo_circuit_client.screens.onlinelist.popup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.qwertyeasy.no_echo_circuit_client.components.DismissSpacer
import com.qwertyeasy.no_echo_circuit_client.components.PixelTextButton
import com.qwertyeasy.no_echo_circuit_client.components.TableTextField
import com.qwertyeasy.no_echo_circuit_client.screens.onlinelist.OnlineListViewModel
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.LightGrey

@Composable
fun UserAddingPopup(onlineViewModel: OnlineListViewModel,
                    rootViewModel: RootViewModel, onDismiss: () -> Unit
){
    Column(modifier = Modifier
        .zIndex(1f)
        .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxWidth()
                .background(LightGrey, shape = RoundedCornerShape(12.dp))
        ) {
            Spacer(modifier = Modifier.weight(0.05f))
            Column(modifier = Modifier.weight(0.85f)) {
                Spacer(Modifier.weight(0.3f))
                PopupInnerTable(onlineViewModel, rootViewModel, Modifier.weight(0.6f))
                Spacer(Modifier.weight(0.2f))
            }
            Spacer(modifier = Modifier.weight(0.05f))
        }
        DismissSpacer(Modifier.weight(0.4f), onDismiss)
    }
}

@Composable
fun PopupInnerTable(onlineViewModel: OnlineListViewModel, rootViewModel: RootViewModel, modifier: Modifier){

    Column(modifier = modifier
        .border(BorderStroke(3.dp, BlackBack))
    ) {
        PopupTableUpperRow(Modifier.weight(0.4f))
        Spacer(Modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(BlackBack)
        )
        AddNickInputBLock(onlineViewModel, Modifier
            .fillMaxWidth()
            .weight(0.25f)
        )
        Spacer(Modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(BlackBack)
        )
        PopupTableBottomRow(onlineViewModel, rootViewModel, Modifier.weight(0.4f))
    }
}

@Composable
fun PopupTableUpperRow(modifier: Modifier){
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.weight(0.3f))
        Spacer(Modifier
            .fillMaxHeight()
            .width(3.dp)
            .background(BlackBack))
        Text(text = "notification", modifier = Modifier.weight(0.7f), color = BlackBack)
    }
}

@Composable
fun PopupTableBottomRow(
    onlineViewModel: OnlineListViewModel, rootViewModel: RootViewModel, modifier: Modifier
){
    Row(modifier = modifier) {
        PixelTextButton(
            text = "try-add",
            modifier = Modifier.weight(0.8f),
            contentColor = LightGrey,
            containerColor = BlackBack,
            onClick = { onlineViewModel.onAddEnterClicked(rootViewModel) }
        )
        Box(Modifier
            .weight(0.2f)
            .fillMaxSize(), contentAlignment = Alignment.Center){
            Text("+")
        }
    }
}

@Composable
fun AddNickInputBLock(onlineViewModel: OnlineListViewModel, modifier: Modifier) {
    val addNickname by onlineViewModel.addNickname.collectAsState()

    Box(modifier = modifier) {
        TableTextField(
            addNickname, LightGrey, BlackBack,
            { onlineViewModel.onAddNicknameChange(it) }
        )
    }
}