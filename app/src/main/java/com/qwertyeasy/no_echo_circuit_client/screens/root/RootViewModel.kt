package com.qwertyeasy.no_echo_circuit_client.screens.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwertyeasy.no_echo_circuit_client.data.ChatMessage
import com.qwertyeasy.no_echo_circuit_client.data.NotificationData
import com.qwertyeasy.no_echo_circuit_client.data.SocketMessage
import com.qwertyeasy.no_echo_circuit_client.data.connectdto.IceCandidateDto
import com.qwertyeasy.no_echo_circuit_client.data.connectdto.IceCandidateMessage
import com.qwertyeasy.no_echo_circuit_client.data.enums.AddingStatus
import com.qwertyeasy.no_echo_circuit_client.data.connectdto.SdpMessage
import com.qwertyeasy.no_echo_circuit_client.data.enums.MessageType
import com.qwertyeasy.no_echo_circuit_client.data.enums.ResponseType
import com.qwertyeasy.no_echo_circuit_client.screens.chat.ChatViewModel
import com.qwertyeasy.no_echo_circuit_client.service.NetworkService
import com.qwertyeasy.no_echo_circuit_client.service.WebRtcClient
import com.qwertyeasy.no_echo_circuit_client.service.blankobservers.RtcSdpObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import java.util.ArrayDeque
import java.util.Queue
import kotlin.collections.emptySet
import kotlin.time.Duration.Companion.seconds

class RootViewModel: ViewModel() {

    private val networkService = NetworkService(
        viewModelScope, { _isFailedToConnect.value = true }
    )

    private val _isFailedToConnect = MutableStateFlow(false)
    val isFailedToConnect = _isFailedToConnect.asStateFlow()
    private val _isLogin = MutableStateFlow(false)
    val isLogin = _isLogin.asStateFlow()
    private val webRtcClient = WebRtcClient.instance
    private var chatViewModel: ChatViewModel? = null
    private var myName: String? = null

    init {
        handleServerMessage()
    }

    fun setChatViewModel(chatViewModel: ChatViewModel){
        this.chatViewModel = chatViewModel
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

    private val _connectedUsers = MutableStateFlow<Set<String>>(emptySet())
    val connectedUsers = _connectedUsers.asStateFlow()

    fun retryConnectToWs(){
        _isFailedToConnect.value = false
        networkService.connect()
    }

    fun onSuccessLogin(){
        _isLogin.value = true
    }

    fun onAddButtonClicked(){
        sendMessage(MessageType.ADD, Json.encodeToString(
            NotificationData(_currentNotification.value!!.userNickname))
        )
        checkNextNotification()
    }

    fun setMyName(name: String){
        myName = name
    }

    fun getMyName(): String{
        return myName!!
    }

    fun addConnectionView(nickname: String){
        _connectedUsers.value += nickname
        println("Добавляю $nickname в список, текущее количество - ${_connectedUsers.value.size}")
    }

    fun removeConnectionView(nickname: String){
        _connectedUsers.value -= nickname
        println("Убираю $nickname из списка, текущее количество - ${_connectedUsers.value.size}")
    }

    fun isUserConnected(nickname: String): Boolean {
        return _connectedUsers.value.contains(nickname)
    }

    fun onConnect(nickname: String, onSuccessConnect: (String) -> Unit){
        println("Отправление запроса к пользователю $nickname")
        if(isUserConnected(nickname)){
            println("Пользователь уже был подключён. Запрос будет проигнорирован")
            chatViewModel!!.currentChatName = nickname
            onSuccessConnect(nickname)
            return
        }
        val onSuccessConnectAndForward: (String) -> Unit = {
            addConnectionView(it)
            chatViewModel!!.currentChatName = it
            onSuccessConnect(it)
        }
        val pc = webRtcClient.createPeerConnection(
            nickname, onSuccessConnectAndForward,
            { receiveMessage(nickname, it) },
            { removeConnectionView(it) }
        ){
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

    fun onAnswerRequest(request: String){
        val answerRequest = Json.decodeFromString<SdpMessage>(request)
        val requestSdp = SessionDescription(
            SessionDescription.Type.OFFER, answerRequest.sdp
        )

        println("Получен запрос о соединении от пользователя ${answerRequest.from}: " +
                "прислали $requestSdp")

        val pc = webRtcClient.createPeerConnection(answerRequest.from,
            { addConnectionView(it) },
            { receiveMessage(answerRequest.from, it) },
            { removeConnectionView(it) }
        ){
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

    fun onAnswerResponse(response: String){
        val sdpMessage = Json.decodeFromString<SdpMessage>(response)
        val responseSdp = SessionDescription(
            SessionDescription.Type.ANSWER, sdpMessage.sdp
        )
        println("Получен ответ от пользователя ${sdpMessage.from}: $responseSdp")

        webRtcClient.setRemoteSdpByNickname(sdpMessage.from, responseSdp)
    }

    fun receiveMessage(nickname: String, buffer: DataChannel.Buffer?){
        buffer?.let {
            val byteArray = ByteArray(buffer.data.remaining())
            buffer.data.get(byteArray)
            val jsonString = String(byteArray, Charsets.UTF_8)
            val chatMessage = Json.decodeFromString<ChatMessage>(jsonString)

            chatViewModel!!.onMessageReceived(chatMessage)
        }
    }

    fun sendWebRTCMessage(chatMessage: ChatMessage){
        webRtcClient.sendMessageToDataChannel(chatMessage.to, chatMessage)
    }

    fun onListRefresh(){
        sendMessage(MessageType.SCAN)
    }

    private val _closePopup = MutableStateFlow(false)
    val closePopup = _closePopup.asStateFlow()

    private val _addStatusIcon = MutableStateFlow(AddingStatus.NONE)
    val addStatusIcon = _addStatusIcon.asStateFlow()

    fun onAddOk(){
        viewModelScope.launch {
            _addStatusIcon.value = AddingStatus.ADD_OK
            delay(2.seconds)
            _closePopup.value = true
            _addStatusIcon.value = AddingStatus.NONE
        }
    }

    fun onAddFail(){
        viewModelScope.launch {
            _addStatusIcon.value = AddingStatus.ADD_FAIL
            delay(4.seconds)
            _addStatusIcon.value = AddingStatus.NONE
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
                    ResponseType.SUCCESS_LOGIN -> onSuccessLogin()
                    ResponseType.USERS_LIST -> onListRefreshReceived(msg.payload!!)
                    ResponseType.NOTIFY_ABOUT_ADD -> onNotifyReceived(msg.payload!!)
                    ResponseType.ADD_OK -> onAddOk()
                    ResponseType.ADD_FAIL -> onAddFail()
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