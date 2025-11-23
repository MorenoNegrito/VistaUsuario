package com.example.vetapp_usuario.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("message") val message: String? = null
)