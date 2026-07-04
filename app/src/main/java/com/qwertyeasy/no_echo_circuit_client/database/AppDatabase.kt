package com.qwertyeasy.no_echo_circuit_client.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.qwertyeasy.no_echo_circuit_client.database.dao.MessageDao
import com.qwertyeasy.no_echo_circuit_client.database.entity.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun messageDao(): MessageDao
}