package com.qwertyeasy.no_echo_circuit_client.screens.chat_management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qwertyeasy.no_echo_circuit_client.components.TitleButton
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatViewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.InterBlack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

@Composable
fun ChatManagementScreen(chatViewModel: ChatViewModel){
    val chatManagementViewModel: ChatManagementViewModel = viewModel(){
        ChatManagementViewModel(chatViewModel)
    }
    Row(Modifier.fillMaxSize().background(BlackBack)) {
        Spacer(Modifier.weight(0.05f))
        Column(Modifier.weight(0.9f)) {
            Spacer(Modifier.weight(0.2f))
            LazyColumn(Modifier.weight(0.7f)) { item {
                    ChatManagementTable(chatManagementViewModel)
            } }
            Spacer(Modifier.weight(0.1f))
        }
        Spacer(Modifier.weight(0.05f))
    }
}

@Composable
fun ChatManagementTable(chatManagementViewModel: ChatManagementViewModel){
    StatisticsBlock(chatManagementViewModel)
    TitleButton("DROP CHAT", Modifier,
        BlackBack, NeonPurple,
        { chatManagementViewModel.cleanCurrentChat() }
    )
}

@Composable
fun StatisticsBlock(chatManagementViewModel: ChatManagementViewModel){
    val chatVolume by chatManagementViewModel.chatVolume.collectAsState()
    val normalized = if(chatVolume < 1000) {
        chatVolume.toString()
    } else {
        "${chatVolume / 1000.00} k"
    }
    Column {
        Text(text = "Messages count:", color = NeonPurple)
        Text(text = normalized, color = NeonPurple, fontFamily = InterBlack)
    }
}