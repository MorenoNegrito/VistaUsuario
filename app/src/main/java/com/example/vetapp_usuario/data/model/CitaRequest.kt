package com.example.vetapp_usuario.data.model
import com.google.gson.annotations.SerializedName

//datos de entrada hrmano
data class CitaRequest(
    @SerializedName("mascotaId") val mascotaId: Int,
    @SerializedName("sucursalId") val sucursalId: Int,
    @SerializedName("veterinarioId") val veterinarioId: Int,
    @SerializedName("fechaHora") val fechaHora: String,
    @SerializedName("motivoCita") val motivoCita: String,
    @SerializedName("mensajeCliente") val mensajeCliente: String?
)