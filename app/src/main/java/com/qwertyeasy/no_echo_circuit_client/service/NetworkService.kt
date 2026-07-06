package com.qwertyeasy.no_echo_circuit_client.service

import com.qwertyeasy.no_echo_circuit_client.data.ResponseMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.net.ConnectException

class NetworkService (
    private val appScope: CoroutineScope,
    val onWsConnectFail: () -> Unit
){

    private val client = HttpClient(){
        install(WebSockets)
    }
    private var session: DefaultClientWebSocketSession? = null

    //может стоит все таки вынести адрес выше в RootViewModel?
    private val serverUrl: String = "ws://10.0.2.2:8888/signal"
//    private val serverUrl: String = "ws://192.168.1.109:8888/signal"
//    private val serveoTunnel = "quinto.serveousercontent.com"
//    private val serverUrl: String = "wss://$serveoTunnel/signal"
    private val _responses = MutableSharedFlow<ResponseMessage>()
    val responses = _responses.asSharedFlow()

    init {
        connect()
    }

    fun connect(){
        appScope.launch {
            try {
                client.webSocket(urlString = serverUrl) {
                    session = this
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue

                        val response = Json.decodeFromString<ResponseMessage>(frame.readText())
                        _responses.emit(response)
                    }
                }
            } catch (_: ConnectException) {
                onWsConnectFail()
            }
        }
    }

    fun sendMessage(message: String){
        appScope.launch {
            if (session != null) {
                session!!.outgoing.send(Frame.Text(message))
            }
        }
    }
}