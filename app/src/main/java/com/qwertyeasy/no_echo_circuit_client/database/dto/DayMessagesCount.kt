package com.qwertyeasy.no_echo_circuit_client.database.dto

import androidx.room.TypeConverters
import com.qwertyeasy.no_echo_circuit_client.database.typeconverters.DateConverters
import java.time.LocalDate

data class DayMessagesCount(

    @field:TypeConverters(DateConverters::class)
    val date: LocalDate,

    val count: Long
)
