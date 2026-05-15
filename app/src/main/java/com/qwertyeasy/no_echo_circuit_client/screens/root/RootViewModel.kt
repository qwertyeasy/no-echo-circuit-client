package com.qwertyeasy.no_echo_circuit_client.screens.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.data.NotificationData
import com.qwertyeasy.no_echo_circuit_client.data.enums.MessageType
import com.qwertyeasy.no_echo_circuit_client.data.SocketMessage
import com.qwertyeasy.no_echo_circuit_client.data.enums.ResponseType
import com.qwertyeasy.no_echo_circuit_client.service.NetworkService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.ArrayDeque
import java.util.Queue
import kotlin.collections.emptyList

class RootViewModel: ViewModel() {

    private val networkService = NetworkService(viewModelScope)

    init {
        handleServerMessage()
    }

    fun sendMessage(type: MessageType, payload: String?){
        val message = SocketMessage(type, payload)
        val json = Json.encodeToString(message)
        networkService.sendMessage(json)
    }

    private val _onlineList = MutableStateFlow<List<String>>(emptyList())
    val onlineList = _onlineList.asStateFlow()

    private val notifyQueue: Queue<NotificationData> = ArrayDeque()
    private val _currentNotification = MutableStateFlow<NotificationData?>(null)
    val currentNotification = _currentNotification.asStateFlow()

    fun onAddButtonClicked(){
        sendMessage(MessageType.ADD, Json.encodeToString(
            NotificationData(_currentNotification.value!!.userNickname))
        )
        checkNextNotification()
    }
    fun checkNextNotification() {
        _currentNotification.value = notifyQueue.poll()
    }

    fun onNotifyDismiss(){
        notifyQueue.clear()
        _currentNotification.value = null
    }

    private fun handleServerMessage(){
        viewModelScope.launch {
            networkService.responses.collect { msg ->
                // TODO: обработать множество сообщений
                when(msg.type){
                    ResponseType.SERVER_CONNECT -> {
                        println("Success connection with server")
                    }
                    ResponseType.SUCCESS_LOGIN -> {
                        //TODO: Написать callback, который благодаря этому переводит дальше по дереву
                    }
                    ResponseType.USERS_LIST -> {
                        _onlineList.value = Json.decodeFromString<List<String>>(msg.payload!!)
                    }
                    ResponseType.NOTIFY_ABOUT_ADD -> {
                        val notifySet = Json.decodeFromString<Set<NotificationData>>(msg.payload!!)
                        notifySet.forEach { notifyQueue.add(it) }
                        if(_currentNotification.value == null) {
                            checkNextNotification()
                        }
                    }
                    ResponseType.ADD_OK -> {
                        println("Пользователь успешно добавлен")
                    }
                    ResponseType.ADD_FAIL -> {
                        println("Пользователь не был добавлен")
                    }
                    ResponseType.USER_CONNECTS -> {
                        //TODO: Обработка коннекта
                    }
                    else -> {}
                }
            }
        }
    }
}