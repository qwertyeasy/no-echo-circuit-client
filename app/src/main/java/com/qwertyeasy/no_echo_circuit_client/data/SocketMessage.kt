package com.qwertyeasy.no_echo_circuit_client.data

import kotlinx.serialization.Serializable

@Serializable
data class SocketMessage(
    var type: MessageType,
    val payload: String?
)