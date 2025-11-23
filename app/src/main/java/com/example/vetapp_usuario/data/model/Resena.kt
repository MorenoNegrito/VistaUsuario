package com.example.vetapp_usuario.data.model
import com.google.gson.annotations.SerializedName

data class Resena(
    @SerializedName("id") val id: Int,
    @SerializedName("citaId") val citaId: Int,
    @SerializedName("veterinarioId") val veterinarioId: Int,
    @SerializedName("usuarioId") val usuarioId: Int,
    @SerializedName("estrellas") val estrellas: Int,
    @SerializedName("comentario") val comentario: String,
    @SerializedName("fechaCreacion") val fechaCreacion: String,
    @SerializedName("usuario") val usuario: Usuario? = null
)