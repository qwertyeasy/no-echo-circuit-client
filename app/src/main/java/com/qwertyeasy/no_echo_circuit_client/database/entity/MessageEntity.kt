package com.qwertyeasy.no_echo_circuit_client.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "messages")
data class MessageEntity(

    //для uuid нужен конвертер
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val fromUser: String,

    val toUser: String,

    var data: String,

    var isCompleted: Boolean,

    var timestamp: Long = System.currentTimeMillis()
)
