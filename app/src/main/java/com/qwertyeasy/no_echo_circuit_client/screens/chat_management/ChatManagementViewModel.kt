package com.qwertyeasy.no_echo_circuit_client.screens.chat_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.database.dto.DayMessagesCount
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ChatManagementViewModel(val chatViewModel: ChatViewModel): ViewModel() {

    private val _chatVolume = MutableStateFlow(0L)
    val chatVolume = _chatVolume.asStateFlow()
    private val _chatDayCount = MutableStateFlow<List<DayMessagesCount>>(listOf())
    val chatDayCount = _chatDayCount.asStateFlow()

    init{ countChatVolume() }

    fun cleanCurrentChat(){
        viewModelScope.launch {
            chatViewModel.messageDao.clearChat(
                chatViewModel.currentChatName!!
            )
        }
    }

    fun getDayCount(){
        viewModelScope.launch {
            _chatDayCount.value = chatViewModel.messageDao.countDayMessages(
                chatViewModel.currentChatName!!
            )
            _chatDayCount.value.forEach {
                println("Статистика за ${it.date}, количество ${it.count}")
            }
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