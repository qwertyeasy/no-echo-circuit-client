package com.qwertyeasy.no_echo_circuit_client.service.blankobservers

import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection

open class ConnectionObserver : PeerConnection.Observer{
    override fun onIceCandidate(candidate: IceCandidate?) {}
    override fun onDataChannel(channel: DataChannel?) {}
    override fun onSignalingChange(p0: PeerConnection.SignalingState?) {}
    override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {}
    override fun onIceConnectionReceivingChange(p0: Boolean) {}
    override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {}
    override fun onIceCandidatesRemoved(p0: Array<out IceCandidate?>?) {}
    override fun onAddStream(p0: MediaStream?) {}
    override fun onRemoveStream(p0: MediaStream?) {}
    override fun onRenegotiationNeeded() {}
}