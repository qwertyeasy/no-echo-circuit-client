package com.qwertyeasy.no_echo_circuit_client.service

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.qwertyeasy.no_echo_circuit_client.database.entity.MessageEntity
import com.qwertyeasy.no_echo_circuit_client.service.blankobservers.ConnectionObserver
import com.qwertyeasy.no_echo_circuit_client.service.blankobservers.DataChannelObserver
import com.qwertyeasy.no_echo_circuit_client.service.blankobservers.RtcSdpObserver
import kotlinx.serialization.json.Json
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.SessionDescription
import java.nio.ByteBuffer
import kotlin.collections.listOf

class WebRtcClient private constructor() {

    private var factory: PeerConnectionFactory? = null
    private val peerConnectionMapByUser = mutableMapOf<String,PeerConnection>()
    private val dataChannelMapByUser = mutableMapOf<String,DataChannel>()

    companion object {
        val instance by lazy { WebRtcClient() }
    }

    fun addIceCandidate(username: String, iceCandidate: IceCandidate){
        val pc = peerConnectionMapByUser[username]
        pc?.addIceCandidate(iceCandidate)
    }

    fun sendMessageToDataChannel(messageEntity: MessageEntity){
        val channel = dataChannelMapByUser[messageEntity.toUser]

        val jsonString = Json.encodeToString(messageEntity)
        val bytes = jsonString.toByteArray(Charsets.UTF_8)
        val buffer = ByteBuffer.allocateDirect(bytes.size)
        buffer.put(bytes)
        buffer.flip()

        channel?.send(DataChannel.Buffer(buffer, false))
    }

    fun parseMessageFromBuffer(buffer: DataChannel.Buffer): MessageEntity{
        val byteArray = ByteArray(buffer.data.remaining())
        buffer.data.get(byteArray)
        val jsonString = String(byteArray, Charsets.UTF_8)

        return Json.decodeFromString<MessageEntity>(jsonString)
    }

    fun createConnectionFactory(context: Context){
        if(factory != null) return

        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .createInitializationOptions())
        val options = PeerConnectionFactory.Options()
        options.disableEncryption = false
        options.disableNetworkMonitor = false

        factory = PeerConnectionFactory.builder()
            .setOptions(options)
            .createPeerConnectionFactory()
    }

    fun setRemoteSdpByNickname(
        remoteNick: String, sdp: SessionDescription
    ){
        val pc = peerConnectionMapByUser[remoteNick]
        pc?.setRemoteDescription(
            object : RtcSdpObserver() {}, sdp
        )
    }

    fun getIceServers(): List<PeerConnection.IceServer>{
        return listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun.cloudflare.com:3478").createIceServer(),
            PeerConnection.IceServer.builder("stun:global.stun.twilio.com:3478").createIceServer(),
            // PeerConnection.IceServer.builder("turn:твой-ip:3478").createIceServer()
        )
    }

    fun connectionResourceDisposal(nickname: String){
        println("Соединение закрыто, очистка ресурсов")
        dataChannelMapByUser.remove(nickname)
        peerConnectionMapByUser.remove(nickname)
    }

    fun createPeerConnection(
        nickname: String, onSuccessConnect: (String) -> Unit, onMessage: (DataChannel.Buffer?) -> Unit,
        onConnectionClosed: (String) -> Unit, onIceCands: (IceCandidate) -> Unit
    ): PeerConnection? {
        val peerFactory = factory ?: throw IllegalStateException("Not initialized")
        val iceServers = getIceServers()

        val rtcConfig = PeerConnection.RTCConfiguration(iceServers).apply {
            bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE
            rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE
            iceTransportsType = PeerConnection.IceTransportsType.ALL
        }
        val observer = object : ConnectionObserver() {
            override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
                println("Статус IceConnection изменился: $p0")
                if(p0 == PeerConnection.IceConnectionState.DISCONNECTED ||
                   p0 == PeerConnection.IceConnectionState.CLOSED ||
                   p0 == PeerConnection.IceConnectionState.FAILED
                ){
                    connectionResourceDisposal(nickname)
                    onConnectionClosed(nickname)
                }
            }
            override fun onIceCandidate(candidate: IceCandidate?) {
                candidate?.let{
                    println("Собран iceCandidate: sdp - ${candidate.sdp}")
                    onIceCands(candidate)
                }
            }
            override fun onDataChannel(channel: DataChannel?) {
                channel?.let { dataChannelMapByUser.put(nickname, channel) }
            }
        }

        //TODO: Возможно у получателя соединениня создается пустой DataChannel
        val peerConnection = peerFactory.createPeerConnection(rtcConfig, observer)
        peerConnection?.let {
            peerConnectionMapByUser[nickname] = peerConnection
            val dataChannel = createDataChannel(nickname, it, onSuccessConnect, onMessage)
            dataChannel?.let { ch -> dataChannelMapByUser.put(nickname, ch) }
        }
        return peerConnection
    }

    fun createSdpForConnection(
        peerConnection: PeerConnection, isOfferSdp: Boolean,
        onCreatedOffer: (SessionDescription) -> Unit
    ){
        val observer = object : RtcSdpObserver() {
            override fun onCreateSuccess(description: SessionDescription?) {
                description?.let {
                    peerConnection.setLocalDescription(
                        object : RtcSdpObserver() {}, it
                    )
                    onCreatedOffer(it)
                }
            }
        }
        if (isOfferSdp) {
            peerConnection.createOffer(observer, MediaConstraints())
        } else {
            peerConnection.createAnswer(observer, MediaConstraints())
        }
    }

    fun createDataChannel(
        nickname: String, peerConnection: PeerConnection, onSuccessConnect: (String) -> Unit,
        onMessage: (DataChannel.Buffer?) -> Unit, label: String = "channel"
    ): DataChannel? {
        val init = DataChannel.Init().apply {
            ordered = true
            maxRetransmitTimeMs = -1
        }
        val dc = peerConnection.createDataChannel(label, init)
        setupDataChannelsObserver(nickname, dc, onSuccessConnect, onMessage)
        return dc
    }

    //TODO: Реализовать методы
    fun setupDataChannelsObserver(
        nickname: String, dataChannel: DataChannel,
        onSuccessConnect: (String) -> Unit, onInputMessage: (DataChannel.Buffer?) -> Unit
    ){
        dataChannel.registerObserver(object: DataChannelObserver(){
            override fun onMessage(p0: DataChannel.Buffer?) {
                println("Получено сообщение по WebRTC")
                onInputMessage(p0)
            }
            override fun onStateChange() {
                println("Состояние DataChannel изменилось, текущее: ${dataChannel.state()}")
                if(dataChannel.state() == DataChannel.State.OPEN){
                    println("Соединение установлено и готово к отправке сообщений")
                    Handler(Looper.getMainLooper()).post {
                        onSuccessConnect(nickname)
                    }
                }
            }
        })
    }
}