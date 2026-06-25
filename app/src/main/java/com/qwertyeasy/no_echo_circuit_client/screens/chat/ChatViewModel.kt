package com.qwertyeasy.no_echo_circuit_client.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.data.ChatMessage
import com.qwertyeasy.no_echo_circuit_client.data.TextMessage
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.collections.plus

class ChatViewModel: ViewModel() {

    private val _chats = MutableStateFlow<Map<String,List<ChatMessage>>>(mapOf())
    val chats = _chats.asStateFlow()
    var currentChatName: String? = null

    fun getCurrentChat(): StateFlow<List<ChatMessage>> {
        return chats.map {
            it[currentChatName] ?: emptyList()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    }

    private val _messageInput = MutableStateFlow("")
    val messageInput = _messageInput.asStateFlow()

    fun onMessageChanged(newText: String){
        _messageInput.value = newText
    }

    fun onMessageReceived(chatMessage: ChatMessage){
        saveMessageToChat(chatMessage.from, chatMessage)
    }

    fun onClipClicked(){
        //TODO: Добавление в отдельное поле и проверка при отправке?
    }

    fun onSendClicked(rootViewModel: RootViewModel){
        println("Отправка сообщения: ${_messageInput.value}")
        val chatMessage = ChatMessage(
            rootViewModel.getMyName(), currentChatName!!,
            TextMessage(_messageInput.value)
        )
        rootViewModel.sendWebRTCMessage(chatMessage)

        saveMessageToChat(currentChatName!!, chatMessage)
        _messageInput.value = ""
    }

    fun saveMessageToChat(nickname: String, chatMessage: ChatMessage){
        if(!_chats.value.contains(nickname)) {
            _chats.value += nickname to listOf(chatMessage)
        } else {
            _chats.value += nickname to (
                    _chats.value[nickname]!!.plus(chatMessage)
            )
        }
    }

    fun onSendPressed(){

    }
}