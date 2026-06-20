package com.qwertyeasy.no_echo_circuit_client

import android.app.Application
import com.qwertyeasy.no_echo_circuit_client.service.WebRtcClient

class ChatApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        WebRtcClient.instance.createConnectionFactory(this)
    }
}