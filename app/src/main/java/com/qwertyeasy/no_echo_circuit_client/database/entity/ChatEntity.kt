package com.qwertyeasy.no_echo_circuit_client.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// на будущее
@Entity(tableName = "chats")
data class ChatEntity(

    @PrimaryKey(autoGenerate = false)
    val chatName: String,
    
    val messageCount: Long,
    
    val lastUpdated: Long
)
