package com.qwertyeasy.no_echo_circuit_client.service.blankobservers

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

open class RtcSdpObserver(): SdpObserver {
    override fun onCreateSuccess(description: SessionDescription?) {}
    override fun onSetSuccess() {}
    override fun onCreateFailure(p0: String?) {}
    override fun onSetFailure(p0: String?) {}
}