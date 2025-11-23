package com.example.vetapp_usuario.data.model

import com.google.gson.annotations.SerializedName

data class Sucursal(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("horarioAtencion") val horarioAtencion: String,
    @SerializedName("serviciosDisponibles") val serviciosDisponibles: String,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("activo") val activo: Boolean? = true,
    @SerializedName("veterinarios") val veterinarios: List<Veterinario>? = null
)