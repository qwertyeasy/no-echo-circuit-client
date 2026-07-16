package com.qwertyeasy.no_echo_circuit_client.screens.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qwertyeasy.no_echo_circuit_client.database.DatabaseProvider
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatScreen
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatViewModel
import com.qwertyeasy.no_echo_circuit_client.screens.chat_management.ChatManagementScreen
import com.qwertyeasy.no_echo_circuit_client.screens.login.LoginScreen
import com.qwertyeasy.no_echo_circuit_client.screens.onlinelist.OnlineListScreen
import com.qwertyeasy.no_echo_circuit_client.screens.root.popup.FailedToConnectPopup
import com.qwertyeasy.no_echo_circuit_client.screens.root.popup.NotificationPopup
import kotlin.system.exitProcess

@Composable
fun RootView(){
    val navController = rememberNavController()
    val rootViewModel: RootViewModel = viewModel()

    val dao = DatabaseProvider.getDatabase(LocalContext.current).messageDao()
    val chatViewModel: ChatViewModel = viewModel(){ ChatViewModel(dao) }

    rootViewModel.setChatViewModel(chatViewModel)

    Box(Modifier.fillMaxSize()) {

        RootPopups(rootViewModel)
        NavHost(
            navController = navController,
            startDestination = Screens.CHAT
        ) {
//            composable(Screens.LOGIN) {
//                LoginScreen(rootViewModel) {
//                    navController.navigate(Screens.ONLINE_LIST) {
//                        popUpTo(Screens.LOGIN) { inclusive = true }
//                    }
//                }
//            }
//            composable(Screens.ONLINE_LIST) {
//                OnlineListScreen(rootViewModel){
//                    navController.navigate(Screens.CHAT){
//                        popUpTo(Screens.ONLINE_LIST) { inclusive = false }
//                    }
//                }
//            }
            composable(Screens.CHAT){
                ChatScreen(chatViewModel, rootViewModel){
                    navController.navigate(Screens.CHAT_MANAGEMENT){
                        popUpTo(Screens.CHAT) { inclusive = false }
                    }
                }
            }
            composable(Screens.CHAT_MANAGEMENT){
                ChatManagementScreen(chatViewModel)
            }
        }
    }
}

@Composable
fun RootPopups(rootViewModel: RootViewModel){
    val currentNotification by rootViewModel.currentNotification.collectAsState()
    val isFailedToConnect by rootViewModel.isFailedToConnect.collectAsState()

    if(isFailedToConnect){
        FailedToConnectPopup(
            { rootViewModel.retryConnectToWs() }, { exitProcess(0) }
        )
    }
    if(currentNotification != null){
        NotificationPopup(
            { rootViewModel.onNotifyDismiss() },
            { rootViewModel.onAddButtonClicked() },
            { rootViewModel.checkNextNotification() }, currentNotification!!
        )
    }
}