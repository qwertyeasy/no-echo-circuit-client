package com.qwertyeasy.no_echo_circuit_client.screens.onlinelist

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qwertyeasy.no_echo_circuit_client.components.SmallPixelTextButton
import com.qwertyeasy.no_echo_circuit_client.components.TitleButton
import com.qwertyeasy.no_echo_circuit_client.components.prepareBorder
import com.qwertyeasy.no_echo_circuit_client.components.threeSidedBorder
import com.qwertyeasy.no_echo_circuit_client.data.enums.MessageType
import com.qwertyeasy.no_echo_circuit_client.screens.onlinelist.popup.UserAddingPopup
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.LightGrey
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

@Composable
fun OnlineListScreen(
    rootViewModel: RootViewModel, onSuccessConnect: (String) -> Unit
){
    val onlineViewModel: OnlineListViewModel = viewModel()
    val onlineList by rootViewModel.onlineList.collectAsState()
    var showPopup by remember { mutableStateOf(false) }
    val closePopup by rootViewModel.closePopup.collectAsState()

    val closePopupCall = {
        showPopup = false
        onlineViewModel.onPopupDismiss()
        rootViewModel.resetAddingIcon()
        rootViewModel.onClosePopupReset()
    }
    if(closePopup){
        closePopupCall()
    }
    if(showPopup) {
        UserAddingPopup(onlineViewModel, rootViewModel, closePopupCall)
    }
    Row(
        Modifier.fillMaxSize().background(BlackBack, RectangleShape)
    ) {
        Spacer(Modifier.weight(0.06f))
        Column(Modifier.weight(0.85f)) {
            Spacer(Modifier.weight(0.2f))

            InnerTable(Modifier.weight(0.8f).fillMaxHeight(),
                rootViewModel, onlineList, onSuccessConnect
            ) { showPopup = true }
            Box(
                modifier = Modifier.weight(0.07f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text("active: ${onlineList.size}", fontSize = 28.sp, color = NeonPurple)
            }
            Spacer(Modifier.weight(0.2f))
        }
        Spacer(Modifier.weight(0.06f))
    }
}

@Composable
fun InnerTable(modifier: Modifier, rootViewModel: RootViewModel, onlineList: List<String>,
               onSuccessConnect: (String) -> Unit, onAddButtonClick: () -> Unit){
    var editing by remember { mutableStateOf<String?>(null) }

    Column(modifier) {
        TitleButton("+USER ", Modifier
            .height(100.dp)
            .border(prepareBorder(NeonPurple)),
           BlackBack, NeonPurple, onAddButtonClick
        )
        PullToRefreshBox(
            // TODO: isRefreshing нужен для защиты от повторных запросов, надо настроить
            isRefreshing = false,
            onRefresh = {
                rootViewModel.onListRefresh()
            },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(Modifier.fillMaxSize()
            ) {
                items(onlineList) { nickname ->
                    UserItem(nickname, rootViewModel,
                        editing == nickname, onSuccessConnect
                    ){
                        editing = it
                    }
                }
                item { Spacer(Modifier) }
            }
        }
    }
}

@Composable
fun UserItem(nickname: String, rootViewModel: RootViewModel,
             isEditing: Boolean, onSuccessConnect: (String) -> Unit, onEditChange: (String?) -> Unit
){
    val connectedUsers by rootViewModel.connectedUsers.collectAsState()
    val isConnected = connectedUsers.contains(nickname)
    val modifier = if(isConnected) Modifier.background(LightGrey)
                    else Modifier.threeSidedBorder(NeonPurple)

    Box(modifier
        .height(60.dp)
        .fillMaxWidth()
        .combinedClickable(
            onClick = {
                rootViewModel.onConnect(nickname, onSuccessConnect)
                if (isEditing) {
                    onEditChange(null)
                }
            },
            onLongClick = { onEditChange(nickname) }
        ),
        contentAlignment = Alignment.CenterStart
    ) {
        if(isEditing) {
            Row(Modifier.background(NeonPurple)) {
                Box (Modifier.weight(0.7f), contentAlignment = Alignment.CenterStart){
                    Text(nickname, Modifier.offset(15.dp), BlackBack)
                }
                SmallPixelTextButton("X", Modifier.weight(0.3f),
                    BlackBack, NeonPurple
                ){
                    rootViewModel.sendMessage(MessageType.REMOVE, nickname)
                    onEditChange(null)
                }
            }
        } else {
            Text(nickname, Modifier.offset(15.dp),
                if(isConnected) BlackBack else NeonPurple)
        }
    }
}