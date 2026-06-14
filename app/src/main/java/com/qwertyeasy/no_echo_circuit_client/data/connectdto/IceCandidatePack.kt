package com.qwertyeasy.no_echo_circuit_client.data.connectdto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.webrtc.IceCandidate

@Serializable
data class IceCandidatePack(
    val from: String,
    val to: String,
    @Contextual val ice: IceCandidate
)