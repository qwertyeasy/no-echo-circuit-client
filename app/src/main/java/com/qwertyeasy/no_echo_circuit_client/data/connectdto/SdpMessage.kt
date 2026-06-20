package com.qwertyeasy.no_echo_circuit_client.data.connectdto

import kotlinx.serialization.Serializable

@Serializable
data class SdpMessage(
    val from: String,
    val to: String,
    val sdp: String
)