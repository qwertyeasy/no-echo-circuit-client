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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.qwertyeasy.no_echo_circuit_client.components.TitleButton
import com.qwertyeasy.no_echo_circuit_client.data.MessageType
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

val list = listOf<String>(
    "alice", "neo", "qwerty", "trinity", "spicy", "judy", "murmelatka",
    "john sina", "pomidor", "lex", "kinoa", "vending machine"
)
@Composable
fun OnlineListScreen(rootViewModel: RootViewModel){
    Row(Modifier.fillMaxSize().background(BlackBack, RectangleShape)) {
        Spacer(Modifier.weight(0.06f))
        Column(Modifier.weight(0.85f)) {
            Spacer(Modifier.weight(0.2f))
            InnerTable(Modifier.weight(0.8f).fillMaxHeight(), rootViewModel)
            Box(modifier = Modifier.weight(0.07f), contentAlignment = Alignment.CenterStart){
                Text("active: ${list.size}", color = NeonPurple)
            }
            Spacer(Modifier.weight(0.2f))
        }
        Spacer(Modifier.weight(0.06f))
    }
}

// для провреки отрисовки
@Composable
fun InnerTable(modifier: Modifier, rootViewModel: RootViewModel){
    Column(modifier) {
        TitleButton("+USER ", Modifier.height(100.dp),
           BlackBack, NeonPurple,
            {
                // смена вида, а после отправка сообщения
               rootViewModel.sendMessage(MessageType.ADD, "")
            }
        )
        LazyColumn(
            Modifier.border(BorderStroke(width = 3.dp, color = NeonPurple), RectangleShape)
        ) {
            items(list) { nickname ->
                UserItem(nickname, rootViewModel)
            }
        }
    }
}

@Composable
fun UserItem(nickname: String, rootViewModel: RootViewModel){
    // функция для создания блока одного пользователя
    Column() {
        Spacer(
            Modifier.fillMaxWidth()
                .height(3.dp).background(NeonPurple)
        )
        Box(
            Modifier.height(60.dp)
                .fillMaxWidth()
                .clickable(onClick = {
                    rootViewModel.sendMessage(
                        MessageType.CONNECT, nickname
                    )
                }),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = nickname,
                color = NeonPurple,
                modifier = Modifier.offset(15.dp)
            )
        }
    }
}