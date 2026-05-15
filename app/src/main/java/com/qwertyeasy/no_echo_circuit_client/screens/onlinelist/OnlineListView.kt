package com.qwertyeasy.no_echo_circuit_client.screens.onlinelist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qwertyeasy.no_echo_circuit_client.components.HorizontalLine
import com.qwertyeasy.no_echo_circuit_client.components.PreparedBorder
import com.qwertyeasy.no_echo_circuit_client.components.TitleButton
import com.qwertyeasy.no_echo_circuit_client.data.enums.MessageType
import com.qwertyeasy.no_echo_circuit_client.screens.onlinelist.popup.UserAddingPopup
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

@Composable
fun OnlineListScreen(rootViewModel: RootViewModel){
    val onlineViewModel: OnlineListViewModel = viewModel()
    val onlineList by rootViewModel.onlineList.collectAsState()
    var showPopup by remember { mutableStateOf(false) }

    if(showPopup) {
        UserAddingPopup(onlineViewModel, rootViewModel, {
            showPopup = false
            onlineViewModel.onPopupDismiss()
        })
    }
    Row(Modifier
        .fillMaxSize()
        .background(BlackBack, RectangleShape)) {
        Spacer(Modifier.weight(0.06f))
        Column(Modifier.weight(0.85f)) {
            Spacer(Modifier.weight(0.2f))
            InnerTable(Modifier.weight(0.8f).fillMaxHeight(),
                rootViewModel, onlineList, {showPopup = true})
            Box(modifier = Modifier.weight(0.07f), contentAlignment = Alignment.CenterStart){
                Text("active: ${onlineList.size}", fontSize = 28.sp, color = NeonPurple)
            }
            Spacer(Modifier.weight(0.2f))
        }
        Spacer(Modifier.weight(0.06f))
    }
}

@Composable
fun InnerTable(modifier: Modifier, rootViewModel: RootViewModel,
               onlineList: List<String>, onAddButtonClick: () -> Unit){
    Column(modifier) {
        TitleButton("+USER ", Modifier.height(100.dp),
           BlackBack, NeonPurple, onAddButtonClick
        )
        LazyColumn(
            Modifier.border(PreparedBorder(NeonPurple), RectangleShape)
        ) {
            items(onlineList) { nickname ->
                UserItem(nickname, rootViewModel)
            }
        }
    }
}

@Composable
fun UserItem(nickname: String, rootViewModel: RootViewModel){
    // функция для создания блока одного пользователя
    Column() {
        HorizontalLine(NeonPurple)
        Box(Modifier.height(60.dp)
                .fillMaxWidth()
                .clickable(onClick = {
                    rootViewModel.sendMessage(
                        MessageType.CONNECT, nickname
                    )
                }), Alignment.CenterStart
        ) {
            Text(
                text = nickname,
                color = NeonPurple,
                modifier = Modifier.offset(15.dp)
            )
        }
    }
}