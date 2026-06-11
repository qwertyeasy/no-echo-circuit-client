package com.qwertyeasy.no_echo_circuit_client.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.qwertyeasy.no_echo_circuit_client.data.ChatMessage
import com.qwertyeasy.no_echo_circuit_client.data.MessageData
import com.qwertyeasy.no_echo_circuit_client.data.TextMessage
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

@Composable
fun ChatScreen(){
    Column(Modifier
        .fillMaxSize()
        .background(BlackBack, RectangleShape)) {
        Spacer(Modifier.weight(0.06f))
        ChatBlock()
        Spacer(Modifier.weight(0.06f))
    }
}

@Composable
fun ChatBlock(){
    val m1 = ChatMessage("1p", "2p", TextMessage("Привет"))
    val m2 = ChatMessage("1p", "2p", TextMessage("Привет"))
    val messageList: List<ChatMessage> = listOf(
        m1.copy(), m2.copy(),
        m1.copy(data = TextMessage("Как дела?")),
        m2.copy(data = TextMessage("Да ничего, нормик")),
        m1.copy(data = TextMessage("Скучный ты, давай пока")))
    val myName = "1p"

    LazyColumn() {
        items(messageList){ item ->
            Box(Modifier.height(50.dp).fillMaxWidth()){
                MessageBox(item.from == myName, item)
            }
        }
    }
}

@Composable
fun MessageBox(isMain: Boolean, item: ChatMessage){
    if (isMain) { Row {
            Spacer(Modifier.weight(0.1f))
            MessageField(Modifier.weight(0.9f), true, item.data) }
    } else { Row {
            MessageField(Modifier.weight(0.9f), false, item.data)
            Spacer(Modifier.weight(0.1f)) }
    }
}

@Composable
fun MessageField(modifier: Modifier, isMain: Boolean, data: MessageData){
    val boxColor = if (isMain) NeonPurple else BlackBack
    val textColor = if (isMain) BlackBack else NeonPurple

    if(data is TextMessage)
    Box(modifier.background(boxColor, RoundedCornerShape(15.dp))){
        Text(text = data.text, color = textColor)
    }
}