package com.qwertyeasy.no_echo_circuit_client.service

import android.content.Context
import com.qwertyeasy.no_echo_circuit_client.service.blankobservers.ConnectionObserver
import com.qwertyeasy.no_echo_circuit_client.service.blankobservers.DataChannelObserver
import com.qwertyeasy.no_echo_circuit_client.service.blankobservers.RtcSdpObserver
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.SessionDescription

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
            PeerConnection.IceServer.builder(
                "stun:stun.l.google.com:19302").createIceServer(),
            // PeerConnection.IceServer.builder(
            // "turn:твой-ip:3478").createIceServer()
        )
    }

    fun createPeerConnection(
        nickname: String, onIceCands: (IceCandidate) -> Unit
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
            }
            override fun onIceCandidate(candidate: IceCandidate?) {
                candidate?.let{ onIceCands(candidate) }
            }
            override fun onDataChannel(channel: DataChannel?) {
                channel?.let { dataChannelMapByUser.put(nickname, channel) }
            }
        }

        val peerConnection = peerFactory.createPeerConnection(rtcConfig, observer)
        peerConnection?.let {
            peerConnectionMapByUser[nickname] = peerConnection
            val dataChannel = createDataChannel(it)
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
        peerConnection: PeerConnection, label: String = "channel"
    ): DataChannel? {
        val init = DataChannel.Init().apply {
            ordered = true
            maxRetransmitTimeMs = -1
        }
        val dc = peerConnection.createDataChannel(label, init)
        setupDataChannelsObserver(dc)
        return dc
    }

    //TODO: Реализовать методы
    fun setupDataChannelsObserver(dataChannel: DataChannel){
        dataChannel.registerObserver(object: DataChannelObserver(){
            override fun onMessage(p0: DataChannel.Buffer?) {
                super.onMessage(p0)
            }

            override fun onStateChange() {
                println("Состояние DataChannel изменилось, текущее: ${dataChannel.state()}")
                if(dataChannel.state() == DataChannel.State.OPEN){
                    println("Соединение установлено и готово к отправке сообщений")
                }
            }
        })
    }
}