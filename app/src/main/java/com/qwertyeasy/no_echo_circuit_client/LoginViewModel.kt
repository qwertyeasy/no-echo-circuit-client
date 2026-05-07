package com.qwertyeasy.no_echo_circuit_client

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {

    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    fun onNicknameChange(changed: String){
        _nickname.value = changed
    }

    fun onEnterClicked(){
        // вход - попытка подключения через ws

    }
}