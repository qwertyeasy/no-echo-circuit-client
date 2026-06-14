package com.qwertyeasy.no_echo_circuit_client.data.connectdto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.webrtc.SessionDescription

@Serializable
data class AnswerRequest(
    val from: String,
    @Contextual val offer: SessionDescription
)
