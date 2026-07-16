package com.qwertyeasy.no_echo_circuit_client.screens.chat_management

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

@Composable
fun ChatManagementScreen(chatViewModel: ChatViewModel){
    val chatManagementViewModel: ChatManagementViewModel = viewModel(){
        ChatManagementViewModel(chatViewModel)
    }
    Row(Modifier.fillMaxSize().background(BlackBack)) {
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
        chatVolume.toString()
    } else {
        "${chatVolume / 1000.00} k"
    }
    Column {
        Text(text = "Messages count:", color = NeonPurple, fontSize = 25.sp)
        Text(text = normalized, color = NeonPurple, fontFamily = InterBlack, fontSize = 60.sp)
        DatesVisualisation(chatManagementViewModel)
    }
}

@Composable
fun DatesVisualisation(chatManagementViewModel: ChatManagementViewModel){
    val dayList by chatManagementViewModel.getDayCount().collectAsState(emptyList())
    var vertOffset = 0
    var horiOffset = 0

    // TODO: перенести в viewmodel
    var pointDateAndCount by remember { mutableStateOf("") }

    val daysIterator = dayList.reversed().iterator()
    LazyRow { item {
        Column {
            while (daysIterator.hasNext()) {
                val next = daysIterator.next()
                println("Текущий день: ${next.date.dayOfWeek}, соответствует - ${next.date.dayOfWeek.value}")
                while ((vertOffset + 1) < next.date.dayOfWeek.value) {
                    println("Недельное смещение: $vertOffset, текущий день: ${next.date.dayOfWeek}")
                    println("Рисуем пустое")
                    EmptyDayPoint(vertOffset, horiOffset)
                    vertOffset++
                }
                println("Недельное смещение: $vertOffset, текущий день: ${next.date.dayOfWeek}")
                println("Рисуем полное")
                FilledDayPoint(vertOffset, horiOffset, next.count,
                    { pointDateAndCount = "${next.date} - ${next.count}" })
                vertOffset++
                if (vertOffset == 7) {
                    vertOffset = 0
                    horiOffset++
                    break
                }
            }
        }}
        //TODO: Нужно настроить проверку того, что последнее в очереди равно текущей дате.
        // Иначе дорисовываем пустые точки.
    }
    if(pointDateAndCount.isNotBlank()){
        Spacer(Modifier.height(24.dp))
        Text(text = pointDateAndCount, color = NeonPurple, fontSize = 25.sp)
    }
}

//TODO: настроить кнопки на точках, которые будут отображать дневное количество
//TODO: Разобраться, оффсеты похоже и не нужны????
@Composable
fun FilledDayPoint(vertOffset: Int, horiOffset: Int, count: Long, onPointClick: () -> Unit){
    val alpha = when{
        //TODO: настроить продуктовые значения, пока тестовые.
//        count >= 300L -> 1f
//        count >= 200L -> 0.8f
//        count >= 150L -> 0.6f
//        count >= 100L -> 0.4f
        count >= 30L -> 1f
        count >= 20L -> 0.8f
        count >= 15L -> 0.6f
        count >= 10L -> 0.4f
        else -> 0.2f
    }
    Box(Modifier.size(30.dp), contentAlignment = Alignment.Center){
        Box(Modifier.size(28.dp)
            .clickable(onClick = onPointClick)
            .background(NeonPurple.copy(alpha = alpha))
        )
    }
}

@Composable
fun EmptyDayPoint(vertOffset: Int, horiOffset: Int){
    Box(Modifier.size(30.dp), contentAlignment = Alignment.Center){
        Box(Modifier.size(4.dp)
            .background(LightGrey.copy(alpha = 0.3f))
        )
    }
}
