package com.qwertyeasy.no_echo_circuit_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.qwertyeasy.no_echo_circuit_client.ui.theme.NoechocircuitclientTheme
import com.qwertyeasy.no_echo_circuit_client.screens.root.RootView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoechocircuitclientTheme() {
                RootView()
            }
        }
    }
}
