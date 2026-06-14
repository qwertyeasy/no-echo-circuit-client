package com.qwertyeasy.no_echo_circuit_client.screens.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qwertyeasy.no_echo_circuit_client.R
import com.qwertyeasy.no_echo_circuit_client.components.HorizontalLine
import com.qwertyeasy.no_echo_circuit_client.components.MultiLineTextField
import com.qwertyeasy.no_echo_circuit_client.data.ChatMessage
import com.qwertyeasy.no_echo_circuit_client.data.MessageData
import com.qwertyeasy.no_echo_circuit_client.data.TextMessage
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.LightGrey
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

@Composable
fun ChatScreen(){
    Box(modifier = Modifier.fillMaxSize().background(BlackBack),
        contentAlignment = Alignment.TopStart
    ) {
        Text(".".repeat(1300), color = LightGrey.copy(alpha = 0.2f), fontSize = 22.sp)
        Column() {
            Spacer(Modifier.weight(0.1f))
            ChatBlock(Modifier.weight(0.9f))
            BottomInputField(Modifier.weight(0.08f))
            Spacer(Modifier.weight(0.03f))
        }
    }
}

@Composable
fun BottomInputField(modifier: Modifier){
    val chatViewModel: ChatViewModel = viewModel()
    val messageInput by chatViewModel.messageInput.collectAsState()

    Column(modifier = modifier) {
        HorizontalLine(NeonPurple)
        Row {
            SquareButton(Modifier.weight(0.12f), R.drawable.clip,
                { chatViewModel.onClipClicked() }, {})
            Box(Modifier.weight(0.8f)) {
                MultiLineTextField(
                    messageInput, BlackBack, NeonPurple,
                    { chatViewModel.onMessageChanged(it) })
            }
            SquareButton(Modifier.weight(0.12f), R.drawable.arrow,
                { chatViewModel.onSendClicked() },
                { chatViewModel.onSendPressed() })
        }
    }
}

@Composable
fun SquareButton(modifier: Modifier, iconId: Int,
                 onClick: () -> Unit, onLongClick: () -> Unit){
    Box(modifier = modifier.fillMaxSize()
        .background(NeonPurple)
        .combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = iconId),
            modifier = Modifier.size(30.dp),
            contentDescription = "SquareButtonIcon",
            colorFilter = ColorFilter.tint(BlackBack)
        )
    }
}

@Composable
fun ChatBlock(modifier: Modifier){
    val m1 = ChatMessage("1p", "2p", TextMessage("Привет"))
    val m2 = ChatMessage("2p", "1p", TextMessage("Здарова, индюк набитый"))
    val messageList = listOf(
        m1.copy(), m2.copy(),
        m1.copy(data = TextMessage("Как дела?")),
        m2.copy(data = TextMessage("Да ничего, нормик")),
        m1.copy(data = TextMessage("Ладно, пока. Хотя нет. Знаешь, о чем я думаю? Да о том, как хочу ударить тебя. А потом выпить лимонаду")),
        m2.copy(data = TextMessage("Эээээ. А ты не прихуел случаем. В заинске за такой базар...")),
        m1.copy(data = TextMessage("Да пошёл ты! Ладно, ты дурик, я вижу это. Все окей. Иди по братски")),
        m2.copy(data = TextMessage("А чууу? Ты че, обоссался что-ли?")),
        m1.copy(data = TextMessage("Нет конечно. Братулёк. Ладо, проехали")),
        m2.copy(data = TextMessage("Ну ты и фрик")),
        m1.copy(data = TextMessage("А я не понялааа")),
        m2.copy(data = TextMessage("А я не понялааа")),
        m1.copy(data = TextMessage("Ну ты не надо даа")),
        m2.copy(data = TextMessage("А че ты мне сделаешь")),
        m1.copy(data = TextMessage("Приеду отпинаю")),
        m2.copy(data = TextMessage("Нет, это я приеду отпинаю")),
        m1.copy(data = TextMessage("Это ты получишь! Понял? От меня! Я тебя так нахлобучу.")),
        )
    val myName = "1p"

    LazyColumn(modifier = modifier) {
        items(messageList){ item ->

            val isMain = item.from == myName
            val alignment = if(isMain) Alignment.CenterEnd else Alignment.CenterStart
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = alignment
            ) {
                MessageField(isMain, item.data)
            }
        }
    }
}

@Composable
fun MessageField(isMain: Boolean, data: MessageData){
    val boxColor = if (isMain) NeonPurple else BlackBack
    val textColor = if (isMain) BlackBack else NeonPurple

    if(data is TextMessage)
        Box(
            modifier = Modifier
                .background(boxColor, shape = RoundedCornerShape(20.dp))
                .padding(10.dp),
        ){
            Text(text = data.text, color = textColor, fontSize = 20.sp, textAlign = TextAlign.Start)
        }
}