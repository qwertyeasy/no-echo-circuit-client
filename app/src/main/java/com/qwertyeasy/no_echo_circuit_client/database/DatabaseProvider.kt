package com.qwertyeasy.no_echo_circuit_client.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase{
        return instance ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "chat-database"
            ).build()
            this.instance = instance
            instance
        }
    }
}