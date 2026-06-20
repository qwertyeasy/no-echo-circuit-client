package com.qwertyeasy.no_echo_circuit_client.data.connectdto

import kotlinx.serialization.Serializable

@Serializable
data class IceCandidateMessage(
    val from: String,
    val to: String,
    val ice: IceCandidateDto
)

@Serializable
data class IceCandidateDto(
    val sdpMid: String,
    val sdpMLineIndex: Int,
    val candidate: String
)