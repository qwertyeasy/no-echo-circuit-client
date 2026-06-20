package com.qwertyeasy.no_echo_circuit_client.data

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage (
    val from: String,
    val to: String,
    val data: MessageData,
    val dateAndTime: Long = System.nanoTime()
)

@Serializable
sealed interface MessageData

@Serializable
data class TextMessage(
    val text: String
) : MessageData

@Serializable
data class ImageMessage(
    val imageBytes: ByteArray
)

@Serializable
data class FileMessage(
    val bytes: ByteArray
)