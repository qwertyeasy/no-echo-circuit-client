package com.qwertyeasy.no_echo_circuit_client.data

import com.qwertyeasy.no_echo_circuit_client.data.enums.ResponseType
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMessage (
    val type: ResponseType,
    val payload: String?
){
}