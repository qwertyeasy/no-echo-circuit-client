package com.qwertyeasy.no_echo_circuit_client.database.typeconverters

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DateConverters {

    @TypeConverter
    fun toDateTime(epoch: Long): LocalDate {
        return Instant.ofEpochMilli(epoch)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}