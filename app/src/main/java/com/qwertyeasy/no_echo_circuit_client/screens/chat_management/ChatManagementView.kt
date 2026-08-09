package com.qwertyeasy.no_echo_circuit_client.screens.chat_management

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qwertyeasy.no_echo_circuit_client.components.SmallPixelTextButton
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatViewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.InterBlack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.LightGrey
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple
import java.time.LocalDate

@Composable
fun ChatManagementScreen(chatViewModel: ChatViewModel){
    val chatManagementViewModel: ChatManagementViewModel = viewModel(){
        ChatManagementViewModel(chatViewModel)
    }
    Row(Modifier
        .fillMaxSize()
        .background(BlackBack)) {
        Spacer(Modifier.weight(0.05f))
        Column(Modifier.weight(0.9f)) {
            Spacer(Modifier.weight(0.2f))
            LazyColumn(Modifier.weight(0.7f)) { item {
                StatisticsBlock(chatManagementViewModel)
            } }
            SmallPixelTextButton("> drop chat", Modifier.height(80.dp),
                BlackBack, NeonPurple,
                { chatManagementViewModel.cleanCurrentChat() }
            )
            Spacer(Modifier.height(140.dp))
        }
        Spacer(Modifier.weight(0.05f))
    }
}

@Composable
fun StatisticsBlock(chatManagementViewModel: ChatManagementViewModel){
    val chatVolume by chatManagementViewModel.countChatVolume().collectAsState(0L)
    val normalized = if(chatVolume < 1000) {
        "$chatVolume"
    } else {
        "${chatVolume / 1000.00} k"
    }
    Column {
        Text(text = "Messages count:", color = NeonPurple, fontSize = 25.sp)
        Text(text = normalized, color = NeonPurple, fontFamily = InterBlack, fontSize = 60.sp)
        Spacer(Modifier.height(16.dp))
        DatesVisualisation(chatManagementViewModel)
    }
}

@Composable
fun DatesVisualisation(chatManagementViewModel: ChatManagementViewModel) {
    // TODO: Вернуть к исходному списку из БД
    val dayList by chatManagementViewModel.getDayCount().collectAsState(emptyList())
//    val dayList = chatManagementViewModel.getTestList()
    if(dayList.isEmpty()) { return }
    val pointDateAndCount by chatManagementViewModel.pointDateAndCount.collectAsState()

    var vertOffset = 0
    var horiOffset = 0
    val afterPointSet = { vertOffset++
        if (vertOffset == 7){ vertOffset = 0; horiOffset++ }}

    val blockWidth = chatManagementViewModel.calcStatsBlockWidth(dayList)
    val daysIterator = dayList.iterator()
    var prevDay: LocalDate? = null

    LazyRow (reverseLayout = true) { item {
        Box (Modifier.height((30*7).dp).width((30*blockWidth).dp)){
            while (daysIterator.hasNext()) {
                // берем следующий элемент
                val next = daysIterator.next()
                if(prevDay == null){
                    prevDay = next.date.minusDays(
                        next.date.dayOfWeek.value.toLong())
                }
                // отрисовка пустых дней до тех пор, пока не дойдем до следующего
                while ((vertOffset + 1) != next.date.dayOfWeek.value
                       || prevDay!!.plusDays(1).dayOfYear < next.date.dayOfYear
                ){
                    EmptyDayPoint(vertOffset, horiOffset)
                    afterPointSet()
                    prevDay = prevDay?.plusDays(1)
                }
                // отрисовка существующего дня
                FilledDayPoint(vertOffset, horiOffset, next.count,
                { chatManagementViewModel.onPointClicked(
                    "D:${next.date}  C:${next.count}") })
                afterPointSet()
                prevDay = next.date
            }
            // список закончился. отрисовка конца таблички до текущей даты
            var pointDate = dayList.last().date
            while (pointDate < LocalDate.now()) {
                EmptyDayPoint(vertOffset, horiOffset)
                pointDate = pointDate.plusDays(1)
                afterPointSet()
            }
            //отрисовка от текущей даты до воскресенья
            while(vertOffset < 7 && vertOffset != 0){
                EmptyDayPoint(vertOffset, horiOffset)
                vertOffset++
            }
        }}
    }
    if (pointDateAndCount.isNotBlank()) {
        Spacer(Modifier.height(24.dp))
        Text(text = pointDateAndCount, color = NeonPurple, fontSize = 22.sp)
    }
}

@Composable
fun FilledDayPoint(vertOffset: Int, horiOffset: Int, count: Long, onPointClick: () -> Unit){
    val alpha = when{
        count >= 200L -> 1f
        count >= 150L -> 0.8f
        count >= 100L -> 0.6f
        count >= 50L -> 0.4f
        else -> 0.2f
    }
    Box(Modifier
        .size(30.dp).absoluteOffset((horiOffset * 30).dp, (vertOffset*30).dp),
        contentAlignment = Alignment.Center
    ){
        Box(Modifier
            .size(26.dp)
            .clickable(onClick = onPointClick)
            .background(NeonPurple.copy(alpha = alpha))
        )
    }
}

@Composable
fun EmptyDayPoint(vertOffset: Int, horiOffset: Int){
    Box(Modifier
        .size(30.dp).absoluteOffset((horiOffset * 30).dp, (vertOffset*30).dp),
        contentAlignment = Alignment.Center
    ){
        Box(Modifier.size(4.dp).background(LightGrey.copy(alpha = 0.2f)))
    }
}
