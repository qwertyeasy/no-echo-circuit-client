package com.qwertyeasy.no_echo_circuit_client.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.database.dao.MessageDao
import com.qwertyeasy.no_echo_circuit_client.database.entity.MessageEntity
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(val messageDao: MessageDao): ViewModel() {

    private val _messageInput = MutableStateFlow("")
    val messageInput = _messageInput.asStateFlow()
    private val _messageSplit = MutableStateFlow(true)
    val messageSplit = _messageSplit.asStateFlow()

    var currentChatName: String? = null

    fun getCurrentChat(): Flow<List<MessageEntity>> {
        return messageDao.getMessages(currentChatName!!)
    }

    fun onMessageSplitSwitch(){
        _messageSplit.value = !_messageSplit.value
    }

    fun onMessageChanged(rootViewModel: RootViewModel, newText: String){
        _messageInput.value = newText
        if(_messageSplit.value) {
            sendMessageToChannel(rootViewModel, false)
        }
    }

    fun onMessageReceived(messageEntity: MessageEntity){
        viewModelScope.launch {
            val notCompleted = messageDao.findNotCompletedMessage(
                messageEntity.fromUser
            )
            if(notCompleted != null){
                notCompleted.data = messageEntity.data
                notCompleted.isCompleted = messageEntity.isCompleted
                messageDao.updateMessage(notCompleted)
            } else {
                messageDao.insertMessage(messageEntity)
            }
        }
    }

    fun saveMessageToDatabase(messageEntity: MessageEntity){
        viewModelScope.launch {
            messageDao.insertMessage(messageEntity)
        }
    }

    fun sendMessageToChannel(
        rootViewModel: RootViewModel, isMessageCompleted: Boolean
    ): MessageEntity{
        val messageEntity = MessageEntity(
            fromUser = rootViewModel.getMyName(),
            toUser = currentChatName!!,
            data = _messageInput.value,
            isCompleted = isMessageCompleted,
        )
        rootViewModel.sendWebRTCMessage(messageEntity)
        return messageEntity
    }

    fun onSendClicked(rootViewModel: RootViewModel){
        println("Отправка сообщения: ${_messageInput.value}")

        val chatMessage = sendMessageToChannel(rootViewModel, true)
        saveMessageToDatabase(chatMessage)
        _messageInput.value = ""
    }

    fun onSendPressed(){
        //TODO: Доп функциональность
    }

    fun onClipClicked(){
        //TODO: Добавление в отдельное поле и проверка при отправке?
    }
}