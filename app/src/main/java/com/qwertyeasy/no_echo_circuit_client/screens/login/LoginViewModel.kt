package com.qwertyeasy.no_echo_circuit_client.screens.login

import androidx.lifecycle.ViewModel
import com.qwertyeasy.no_echo_circuit_client.data.enums.MessageType
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {

    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    fun onNicknameChange(changed: String){
        _nickname.value = changed
    }

    fun onEnterClicked(rootViewModel: RootViewModel){
        rootViewModel.sendMessage(
            MessageType.AUTH, _nickname.value
        )
    }
}