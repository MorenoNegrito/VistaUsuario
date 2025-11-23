package com.example.vetapp_usuario.data.model

import com.google.gson.annotations.SerializedName

data class ResenaRequest(
    @SerializedName("citaId") val citaId: Int,
    @SerializedName("estrellas") val estrellas: Int,
    @SerializedName("comentario") val comentario: String
)