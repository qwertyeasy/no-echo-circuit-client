package com.qwertyeasy.no_echo_circuit_client.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.qwertyeasy.no_echo_circuit_client.database.dto.DayMessagesCount
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

    @Query("""
        SELECT * FROM messages
        WHERE fromUser = :chatName
        AND isCompleted = 0
        ORDER BY timestamp DESC
        LIMIT 1
    """)
    suspend fun findNotCompletedMessage(chatName: String): MessageEntity?

    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Query("""
        DELETE FROM messages
        WHERE fromUser = :chatName
        OR toUser = :chatName
    """)
    suspend fun clearChat(chatName: String)

    @Query("""
        SELECT COUNT(*)
        FROM messages
        WHERE fromUser = :chatName
        OR toUser = :chatName
    """)
    suspend fun getChatVolume(chatName: String): Long

    @Query("""
        SELECT (timestamp / 86400000 * 86400000) AS date, COUNT(*) AS count
        FROM messages
        WHERE fromUser = :chatName
        OR toUser = :chatName
        GROUP BY(timestamp / 86400000 * 86400000)
    """)
    suspend fun countDayMessages(chatName: String): List<DayMessagesCount>
}