package com.qwertyeasy.no_echo_circuit_client.screens.chat_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatManagementViewModel(val chatViewModel: ChatViewModel): ViewModel() {

    private val _chatVolume = MutableStateFlow(0L)
    val chatVolume = _chatVolume.asStateFlow()

    init{ countChatVolume() }

    fun cleanCurrentChat(){
        viewModelScope.launch {
            chatViewModel.messageDao.clearChat(
                chatViewModel.currentChatName!!
            )
        }
    }

    private fun countChatVolume(){
        viewModelScope.launch {
            _chatVolume.value = chatViewModel.messageDao.getChatVolume(
                chatViewModel.currentChatName!!
            )
        }
    }
}