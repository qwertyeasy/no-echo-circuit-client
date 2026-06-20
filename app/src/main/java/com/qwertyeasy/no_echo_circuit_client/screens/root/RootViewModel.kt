package com.qwertyeasy.no_echo_circuit_client.screens.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.data.NotificationData
import com.qwertyeasy.no_echo_circuit_client.data.SocketMessage
import com.qwertyeasy.no_echo_circuit_client.data.connectdto.IceCandidateDto
import com.qwertyeasy.no_echo_circuit_client.data.connectdto.SdpMessage
import com.qwertyeasy.no_echo_circuit_client.data.connectdto.IceCandidateMessage
import com.qwertyeasy.no_echo_circuit_client.data.enums.MessageType
import com.qwertyeasy.no_echo_circuit_client.data.enums.ResponseType
import com.qwertyeasy.no_echo_circuit_client.service.NetworkService
import com.qwertyeasy.no_echo_circuit_client.service.WebRtcClient
import com.qwertyeasy.no_echo_circuit_client.service.blankobservers.RtcSdpObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import java.util.ArrayDeque
import java.util.Queue

class RootViewModel: ViewModel() {

    private val networkService = NetworkService(viewModelScope)
    private val webRtcClient = WebRtcClient.instance
    private var myName: String? = null

    init {
        handleServerMessage()
    }

    fun sendMessage(type: MessageType, payload: String? = null){
        val message = SocketMessage(type, payload)
        val json = Json.encodeToString(message)
        networkService.sendMessage(json)
    }

    private val _onlineList = MutableStateFlow<List<String>>(emptyList())
    val onlineList = _onlineList.asStateFlow()

    private val notifyQueue: Queue<NotificationData> = ArrayDeque()
    private val _currentNotification = MutableStateFlow<NotificationData?>(null)
    val currentNotification = _currentNotification.asStateFlow()

    fun onAddButtonClicked(){
        sendMessage(MessageType.ADD, Json.encodeToString(
            NotificationData(_currentNotification.value!!.userNickname))
        )
        checkNextNotification()
    }

    fun setMyName(name: String){
        myName = name
    }

    // 1 шаг - отправка запроса
    fun onConnect(nickname: String){
        println("Отправление запроса к пользователю $nickname")

        val pc = webRtcClient.createPeerConnection(nickname){
            sendMessage(MessageType.ICE, Json.encodeToString(
                IceCandidateMessage(
                    myName!!, nickname, IceCandidateDto(
                        it.sdpMid, it.sdpMLineIndex, it.sdp
                    )
                )
            ))
        }!!
        webRtcClient.createSdpForConnection(peerConnection = pc, isOfferSdp = true){
            sendMessage(MessageType.CONNECT, Json.encodeToString(
                SdpMessage(myName!!, nickname, it.description)
            ))
        }
    }

    // 3 шаг - получение запроса с оффером, создание ответа и отправка
    fun onAnswerRequest(request: String){
        val answerRequest = Json.decodeFromString<SdpMessage>(request)
        val requestSdp = SessionDescription(
            SessionDescription.Type.OFFER, answerRequest.sdp
        )

        println("Получен запрос о соединении от пользователя ${answerRequest.from}: " +
                "прислали $requestSdp")

        val pc = webRtcClient.createPeerConnection(answerRequest.from){
            sendMessage(MessageType.ICE, Json.encodeToString(
                IceCandidateMessage(
                    myName!!, answerRequest.from, IceCandidateDto(
                        it.sdpMid, it.sdpMLineIndex, it.sdp
                    )
                )
            ))
        }
        pc?.setRemoteDescription(
            object : RtcSdpObserver() {}, requestSdp
        )
        webRtcClient.createSdpForConnection(peerConnection = pc!!, isOfferSdp = false){
            sendMessage(MessageType.ANSWER, Json.encodeToString(
                SdpMessage(
                    from = myName!!,
                    to = answerRequest.from,
                    sdp = it.description
                )
            ))
        }
    }

    // 5 шаг - получение ответа, готов к подключению
    fun onAnswerResponse(response: String){
        val sdpMessage = Json.decodeFromString<SdpMessage>(response)
        val responseSdp = SessionDescription(
            SessionDescription.Type.ANSWER, sdpMessage.sdp
        )
        println("Получен ответ от пользователя ${sdpMessage.from}: $responseSdp")

        webRtcClient.setRemoteSdpByNickname(sdpMessage.from, responseSdp)
    }

    fun onListRefresh(){
        sendMessage(MessageType.SCAN)
    }

    private val _closePopup = MutableStateFlow(false)
    val closePopup = _closePopup.asStateFlow()

    fun onAddOk(){
        viewModelScope.launch {
            delay(2000)
            _closePopup.value = true
        }
    }

    fun onClosePopupReset(){
        _closePopup.value = false
    }

    fun onListRefreshReceived(payload: String){
        _onlineList.value = Json.decodeFromString<List<String>>(payload)
    }

    fun checkNextNotification() {
        _currentNotification.value = notifyQueue.poll()
    }

    fun onNotifyDismiss(){
        notifyQueue.clear()
        _currentNotification.value = null
    }

    fun onNotifyReceived(payload: String){
        val notifySet = Json.decodeFromString<Set<NotificationData>>(payload)
        notifySet.forEach { notifyQueue.add(it) }
        if(_currentNotification.value == null) {
            checkNextNotification()
        }
    }

    fun onIceReceived(payload: String){
        val msg = Json.decodeFromString<IceCandidateMessage>(payload)
        println("Получен айс-кандидат от пользователя ${msg.from}")

        val iceCandidate = IceCandidate(
            msg.ice.sdpMid, msg.ice.sdpMLineIndex, msg.ice.candidate
        )
        webRtcClient.addIceCandidate(msg.from, iceCandidate)
    }

    private fun handleServerMessage(){
        viewModelScope.launch {
            networkService.responses.collect { msg ->
                // TODO: обработать множество сообщений
                when(msg.type){
                    ResponseType.SERVER_CONNECT -> println("Success connection with server")
                    ResponseType.SUCCESS_LOGIN -> println("Пользователь успешно залогинился")
                    ResponseType.USERS_LIST -> onListRefreshReceived(msg.payload!!)
                    ResponseType.NOTIFY_ABOUT_ADD -> onNotifyReceived(msg.payload!!)
                    ResponseType.ADD_OK -> onAddOk()
                    ResponseType.ADD_FAIL -> println("Пользователь не был добавлен")
                    ResponseType.USER_OFFLINE -> println("Запрошенный пользователь отключился")
                    ResponseType.ANSWER_REQUEST -> onAnswerRequest(msg.payload!!)
                    ResponseType.ANSWER_RESPONSE -> onAnswerResponse(msg.payload!!)
                    ResponseType.ICE -> onIceReceived(msg.payload!!)
                    ResponseType.ERROR -> println("Произошла неизвестная ошибка")
                }
            }
        }
    }
}