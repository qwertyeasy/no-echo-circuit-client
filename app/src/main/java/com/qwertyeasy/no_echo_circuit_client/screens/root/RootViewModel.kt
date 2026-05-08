package com.qwertyeasy.no_echo_circuit_client.screens.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.data.MessageType
import com.qwertyeasy.no_echo_circuit_client.data.SocketMessage
import com.qwertyeasy.no_echo_circuit_client.service.NetworkService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RootViewModel: ViewModel() {

    private val networkService = NetworkService(viewModelScope)

    init {
        networkService.connect()
    }

    fun sendMessage(type: MessageType, payload: String?){
        val message = SocketMessage(type, payload)
        val json = Json.encodeToString(message)
        networkService.sendMessage(json)
    }
}