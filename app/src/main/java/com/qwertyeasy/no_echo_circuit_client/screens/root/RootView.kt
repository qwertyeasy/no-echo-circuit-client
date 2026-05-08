package com.qwertyeasy.no_echo_circuit_client.screens.root

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qwertyeasy.no_echo_circuit_client.data.Screens
import com.qwertyeasy.no_echo_circuit_client.screens.login.LoginScreen
import com.qwertyeasy.no_echo_circuit_client.screens.onlinelist.OnlineListScreen

@Composable
fun RootView(){
    val navController = rememberNavController()
    val rootViewModel: RootViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screens.LOGIN
    ){
        composable(Screens.LOGIN) {
            LoginScreen(rootViewModel, {
                navController.navigate(Screens.ONLINE_LIST){
                    popUpTo(Screens.LOGIN) { inclusive = true }
                }
            })
        }
        composable(Screens.ONLINE_LIST) {
            OnlineListScreen(rootViewModel)
        }
    }
}