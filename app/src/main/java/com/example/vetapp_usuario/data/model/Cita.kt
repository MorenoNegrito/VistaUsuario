package com.example.vetapp_usuario.data.model
import com.google.gson.annotations.SerializedName

data class Cita(
    @SerializedName("id") val id: Int,
    @SerializedName("mascotaId") val mascotaId: Int,
    @SerializedName("sucursalId") val sucursalId: Int,
    @SerializedName("veterinarioId") val veterinarioId: Int,
    @SerializedName("usuarioId") val usuarioId: Int,
    @SerializedName("fechaHora") val fechaHora: String,
    @SerializedName("motivoCita") val motivoCita: String,
    @SerializedName("mensajeCliente") val mensajeCliente: String?,
    @SerializedName("estado") val estado: String,
    @SerializedName("diagnostico") val diagnostico: String?,
    @SerializedName("tratamiento") val tratamiento: String?,
    @SerializedName("observaciones") val observaciones: String?,
    @SerializedName("resenaVeterinario") val resenaVeterinario: String?,
    @SerializedName("mascota") val mascota: Mascota? = null,
    @SerializedName("veterinario") val veterinario: Veterinario? = null,
    @SerializedName("sucursal") val sucursal: Sucursal? = null
)