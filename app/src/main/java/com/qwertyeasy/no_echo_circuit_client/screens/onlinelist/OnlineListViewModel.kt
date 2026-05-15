package com.qwertyeasy.no_echo_circuit_client.screens.onlinelist

import androidx.lifecycle.ViewModel
import com.qwertyeasy.no_echo_circuit_client.data.NotificationData
import com.qwertyeasy.no_echo_circuit_client.data.enums.MessageType
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OnlineListViewModel: ViewModel() {

    private val _addNickname = MutableStateFlow("")
    val addNickname = _addNickname.asStateFlow()

    private val _addDescription = MutableStateFlow("")
    val addDescription = _addDescription.asStateFlow()

    // TODO: нужно настроить DI через Hilt и внедрять networkService
    //  для отправки сообщения сканирования через networkService
//    init{
//        runScanRequests()
//    }
//
//    private fun runScanRequests(){
//        viewModelScope.launch {
//            // как-то производить отправку сообщений к серверу
//        }
//    }

    fun onAddNicknameChange(changed: String){
        _addNickname.value = changed
    }

    fun onAddDescriptionChange(changed: String){
        _addDescription.value = changed
    }

    fun onPopupDismiss(){
        _addNickname.value = ""
        _addDescription.value = ""
    }

    fun onAddEnterClicked(rootViewModel: RootViewModel){
        val notifyDataJson = Json.encodeToString(
            NotificationData(
                _addNickname.value, _addDescription.value
            ))
        rootViewModel.sendMessage(
            MessageType.ADD, notifyDataJson
        )
    }
}