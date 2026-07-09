package com.qwertyeasy.no_echo_circuit_client.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwertyeasy.no_echo_circuit_client.R
import com.qwertyeasy.no_echo_circuit_client.components.ExtendingTextField
import com.qwertyeasy.no_echo_circuit_client.components.HorizontalLine
import com.qwertyeasy.no_echo_circuit_client.components.SquareButton
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.InterBlack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.LightGrey
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple
import com.qwertyeasy.no_echo_circuit_client.ui.theme.PixelCyr

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel, rootViewModel: RootViewModel, onTitleClick: () -> Unit
){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(BlackBack),
        contentAlignment = Alignment.TopStart
    ) {
        ChatBackground()
        Column() {
            Spacer(Modifier.weight(0.03f))
            ChatTitle(Modifier.weight(0.08f), chatViewModel, onTitleClick)
            ChatBlock(Modifier.weight(0.9f), chatViewModel, rootViewModel)
            BottomInputField(chatViewModel, rootViewModel)
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
fun ChatTitle(modifier: Modifier, chatViewModel: ChatViewModel, onTitleClick: () -> Unit){
    val chatTitle = chatViewModel.currentChatName!!
    val messageSplit by chatViewModel.messageSplit.collectAsState()
    val splitIcon = if(messageSplit) R.drawable.add_ok else R.drawable.add_fail

    Row(
        modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ){
        Spacer(Modifier.weight(0.1f))
        Box(Modifier.weight(0.9f).clickable(onClick = onTitleClick)) {
            Text(chatTitle, color = NeonPurple, fontFamily = InterBlack, fontSize = 30.sp)
        }
        SquareButton(Modifier.weight(0.1f), splitIcon, Color.Transparent,
            NeonPurple, 50.dp,
            { chatViewModel.onMessageSplitSwitch() }, {})
        Spacer(Modifier.weight(0.1f))
    }
    HorizontalLine(NeonPurple)
    HorizontalLine(BlackBack)
}

@Composable
fun ChatBackground(){
    Text(".".repeat(1300), color = LightGrey.copy(alpha = 0.2f), fontSize = 22.sp)
}

@Composable
fun BottomInputField(chatViewModel: ChatViewModel, rootViewModel: RootViewModel
){
    val messageInput by chatViewModel.messageInput.collectAsState()

    Column() {
        HorizontalLine(BlackBack)
        HorizontalLine(NeonPurple)
        Row(verticalAlignment = Alignment.Bottom) {
            SquareButton(Modifier.size(50.dp), R.drawable.clip, NeonPurple,
                BlackBack, 30.dp,{ chatViewModel.onClipClicked() }, {})
            Box(Modifier.weight(0.8f)) {
                ExtendingTextField(messageInput, BlackBack, NeonPurple,
                    { chatViewModel.onMessageChanged(rootViewModel, it) })
            }
            SquareButton(Modifier.size(50.dp), R.drawable.arrow, NeonPurple,
                BlackBack, 30.dp, { chatViewModel.onSendClicked(rootViewModel) },
                { chatViewModel.onSendPressed() })
        }
    }
}

@Composable
fun ChatBlock(modifier: Modifier, chatViewModel: ChatViewModel, rootViewModel: RootViewModel){
    val chatList by chatViewModel.getCurrentChat().collectAsState(emptyList())
    val myName = rootViewModel.getMyName()

    LazyColumn(modifier = modifier, reverseLayout = true) {
        items(chatList.reversed()){ item ->

            val isMain = item.fromUser == myName
            val alignment = if(isMain) Alignment.CenterEnd else Alignment.CenterStart
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = alignment
            ) {
                MessageField(isMain, item.isCompleted, item.data)
            }
        }
    }
}

@Composable
fun MessageField(isMain: Boolean, isCompleted: Boolean, data: String){
    val boxColor = if (isMain) NeonPurple else BlackBack
    val textColor = if (isMain) BlackBack else NeonPurple

    Box(
        modifier = Modifier
            .background(boxColor, shape = RoundedCornerShape(20.dp))
            .padding(10.dp),
    ) {
        if(isMain || isCompleted) {
            Text(text = data, color = textColor, fontSize = 16.sp,
                 fontFamily = PixelCyr, textAlign = TextAlign.Start)
        } else {
            Text(text = buildAnnotatedString {
                append(data)
                addStyle(
                    style = SpanStyle(background = NeonPurple, color = BlackBack),
                    start = data.length-1,
                    end = data.length
                ) },
                color = textColor, fontSize = 16.sp,
                fontFamily = PixelCyr, textAlign = TextAlign.Start
            )
        }
    }
}