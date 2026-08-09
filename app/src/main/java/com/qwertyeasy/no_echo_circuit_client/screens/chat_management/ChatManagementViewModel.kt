package com.qwertyeasy.no_echo_circuit_client.screens.chat_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.database.dto.DayMessagesCount
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class ChatManagementViewModel(val chatViewModel: ChatViewModel): ViewModel() {

    private val _pointDateAndCount = MutableStateFlow("")
    val pointDateAndCount = _pointDateAndCount.asStateFlow()
    fun onPointClicked(dateAndCount: String){
        _pointDateAndCount.value = dateAndCount
    }

    //TODO: Удалить после полной настройки
    fun getTestList(): List<DayMessagesCount>{
        val date: LocalDate = LocalDate.now()
        return listOf(
            DayMessagesCount(date.minusDays(26), 14L),
            DayMessagesCount(date.minusDays(17), 129L),
            DayMessagesCount(date.minusDays(15), 174L),
            DayMessagesCount(date.minusDays(7), 230L),
            DayMessagesCount(date.minusDays(4), 312L),
        )
    }

    fun calcStatsBlockWidth(dayList: List<DayMessagesCount>): Int{
        if(dayList.isEmpty()){ return 1 }

        var first = dayList.first().date
        first = first.minusDays(first.dayOfWeek.value.toLong()-1)
        return ChronoUnit.WEEKS.between(
            first, LocalDate.now()
        ).toInt() + 1
    }

    fun cleanCurrentChat(){
        viewModelScope.launch {
            chatViewModel.messageDao.clearChat(
                chatViewModel.currentChatName!!
            )
        }
    }

    fun getDayCount(): Flow<List<DayMessagesCount>>{
        return chatViewModel.messageDao.countDayMessages(
            chatViewModel.currentChatName!!
        )
    }

    fun countChatVolume(): Flow<Long> {
        return chatViewModel.messageDao.getChatVolume(
            chatViewModel.currentChatName!!
        )
    }
}