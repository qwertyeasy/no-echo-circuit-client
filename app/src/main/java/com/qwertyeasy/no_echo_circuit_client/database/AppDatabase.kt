package com.qwertyeasy.no_echo_circuit_client.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.qwertyeasy.no_echo_circuit_client.database.dao.MessageDao
import com.qwertyeasy.no_echo_circuit_client.database.entity.MessageEntity
import com.qwertyeasy.no_echo_circuit_client.database.typeconverters.DateConverters

@Database(
    entities = [MessageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun messageDao(): MessageDao
}