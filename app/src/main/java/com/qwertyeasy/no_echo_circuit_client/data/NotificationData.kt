package com.qwertyeasy.no_echo_circuit_client.data

import kotlinx.serialization.Serializable

@Serializable
data class NotificationData(
    val userNickname: String,
    val description: String? = null
)
