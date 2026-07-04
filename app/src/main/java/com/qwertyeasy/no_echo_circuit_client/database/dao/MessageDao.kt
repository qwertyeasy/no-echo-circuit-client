package com.qwertyeasy.no_echo_circuit_client.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.qwertyeasy.no_echo_circuit_client.database.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("""
        SELECT * FROM messages
        WHERE fromUser = :chatName
        OR toUser = :chatName
        ORDER BY timestamp
    """)
    fun getMessages(chatName: String): Flow<List<MessageEntity>>

    @Query(
        """
        SELECT * FROM messages
        WHERE fromUser = :chatName
        AND isCompleted = 0
        ORDER BY timestamp DESC
        LIMIT 1
    """
    )
    suspend fun findNotCompletedMessage(chatName: String): MessageEntity?

    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Update
    suspend fun updateMessage(message: MessageEntity)
}