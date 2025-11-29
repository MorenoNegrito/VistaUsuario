package com.example.vetapp_usuario.data.model

import com.google.gson.annotations.SerializedName

data class ResenaRequest(
    @SerializedName("citaId")
    val citaId: Int,

    @SerializedName("estrellas")
    val estrellas: Int,  // 1-5

    @SerializedName("comentario")
    val comentario: String
)
