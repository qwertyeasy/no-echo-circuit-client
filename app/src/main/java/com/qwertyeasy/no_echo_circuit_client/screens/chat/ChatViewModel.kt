package com.qwertyeasy.no_echo_circuit_client.screens.chat

import androidx.lifecycle.ViewModel
import com.qwertyeasy.no_echo_circuit_client.data.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel: ViewModel() {

    private val chatMapByUsers = HashMap<String, ArrayList<ChatMessage>>()
    private val _chatList = MutableStateFlow(ArrayList<ChatMessage>())
    val chatList = _chatList.asStateFlow()

    private val _messageInput = MutableStateFlow("")
    val messageInput = _messageInput.asStateFlow()

    private val currentChatUser = MutableStateFlow("")

    fun onMessageChanged(newText: String){
        _messageInput.value = newText
    }

    fun onMessageReceived(chatMessage: ChatMessage){
        _chatList.value.add(chatMessage)
    }

    fun onChatEnter(userNick: String){
        _chatList.value = chatMapByUsers.computeIfAbsent(
            userNick, { ArrayList<ChatMessage>() }
        )
    }

    fun onClipClicked(){

    }

    fun onSendClicked(){

    }

    fun onSendPressed(){

    }
}