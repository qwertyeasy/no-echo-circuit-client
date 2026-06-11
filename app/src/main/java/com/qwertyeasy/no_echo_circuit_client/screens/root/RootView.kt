package com.qwertyeasy.no_echo_circuit_client.screens.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qwertyeasy.no_echo_circuit_client.components.DismissSpacer
import com.qwertyeasy.no_echo_circuit_client.components.SmallPixelTextButton
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatScreen
import com.qwertyeasy.no_echo_circuit_client.screens.login.LoginScreen
import com.qwertyeasy.no_echo_circuit_client.screens.onlinelist.OnlineListScreen
import com.qwertyeasy.no_echo_circuit_client.ui.theme.BlackBack
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NeonPurple

@Composable
fun RootView(){
    val navController = rememberNavController()
    val rootViewModel: RootViewModel = viewModel()

    val currentNotification by rootViewModel.currentNotification.collectAsState()

    Box(Modifier.fillMaxSize()) {
        if(currentNotification != null){
            //TODO: Вынести этот модуль в другой файл
            Column(Modifier.zIndex(1f).fillMaxSize()) {
                DismissSpacer(Modifier.weight(0.7f), { rootViewModel.onNotifyDismiss() })
                Column(Modifier.weight(0.3f).fillMaxSize().background(NeonPurple)) {
                    Text("Your contact was saved by", fontSize = 30.sp)
                    Text(currentNotification!!.userNickname, fontSize = 45.sp)
                    currentNotification!!.description?.let { Text(it, fontSize = 30.sp) }

                    Row() {
                        SmallPixelTextButton("add", Modifier.weight(0.5f), NeonPurple,
                            BlackBack, { rootViewModel.onAddButtonClicked() })
                        SmallPixelTextButton("ignore", Modifier.weight(0.5f), NeonPurple,
                            BlackBack, { rootViewModel.checkNextNotification() })
                    }
                    Spacer(Modifier.height(10.dp))
                }
                DismissSpacer(Modifier.weight(0.1f), { rootViewModel.onNotifyDismiss() })
            }
        }

        //TODO: Вернуть начальное startDestination - LOGIN
        NavHost(
            navController = navController,
            startDestination = Screens.CHAT
        ) {
            composable(Screens.LOGIN) {
                LoginScreen(rootViewModel, {
                    navController.navigate(Screens.ONLINE_LIST) {
                        popUpTo(Screens.LOGIN) { inclusive = true }
                    }
                })
            }
            composable(Screens.ONLINE_LIST) {
                OnlineListScreen(rootViewModel)
            }
            composable(Screens.CHAT){
                ChatScreen()
            }
        }
    }
}