package com.qwertyeasy.no_echo_circuit_client.screens.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatScreen
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatViewModel
import com.qwertyeasy.no_echo_circuit_client.screens.login.LoginScreen
import com.qwertyeasy.no_echo_circuit_client.screens.onlinelist.OnlineListScreen
import com.qwertyeasy.no_echo_circuit_client.screens.root.popup.NotificationPopup

@Composable
fun RootView(){
    val navController = rememberNavController()
    val rootViewModel: RootViewModel = viewModel()
    val chatViewModel: ChatViewModel = viewModel()
    rootViewModel.setChatViewModel(chatViewModel)

    val currentNotification by rootViewModel.currentNotification.collectAsState()

    Box(Modifier.fillMaxSize()) {

        if(currentNotification != null){
            NotificationPopup(rootViewModel, currentNotification!!)
        }
        NavHost(
            navController = navController,
            startDestination = Screens.LOGIN
        ) {
            composable(Screens.LOGIN) {
                LoginScreen(rootViewModel, {
                    navController.navigate(Screens.ONLINE_LIST) {
                        popUpTo(Screens.LOGIN) { inclusive = true }
                    }
                })
            }
            composable(Screens.ONLINE_LIST) {
                OnlineListScreen(rootViewModel){
                    navController.navigate(Screens.CHAT){
                        popUpTo(Screens.ONLINE_LIST) { inclusive = false }
                    }
                }
            }
            composable(Screens.CHAT){
                ChatScreen(chatViewModel, rootViewModel)
            }
        }
    }
}