package com.qwertyeasy.no_echo_circuit_client.service.blankobservers

import org.webrtc.DataChannel

open class DataChannelObserver : DataChannel.Observer{
    override fun onMessage(p0: DataChannel.Buffer?) {}
    override fun onStateChange() {}
    override fun onBufferedAmountChange(p0: Long) {}
}