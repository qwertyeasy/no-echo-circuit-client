package com.qwertyeasy.no_echo_circuit_client.screens.chat_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.database.dto.DayMessagesCount
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ChatManagementViewModel(val chatViewModel: ChatViewModel): ViewModel() {

    fun cleanCurrentChat(){
        viewModelScope.launch {
            chatViewModel.messageDao.clearChat(
                chatViewModel.currentChatName!!
            )
        }
    }

    fun getDayCount(): Flow<List<DayMessagesCount>>{
        return chatViewModel.messageDao.countDayMessages(
            chatViewModel.currentChatName!!
        )
    }

    fun countChatVolume(): Flow<Long> {
        return chatViewModel.messageDao.getChatVolume(
            chatViewModel.currentChatName!!
        )
    }
}