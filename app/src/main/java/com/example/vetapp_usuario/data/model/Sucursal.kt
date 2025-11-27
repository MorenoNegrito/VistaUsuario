package com.example.vetapp_usuario.data.model

import com.google.gson.annotations.SerializedName

data class Sucursal(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("horarioAtencion") val horarioAtencion: String,

    // Campos opcionales que pueden venir del backend
    @SerializedName("serviciosDisponibles") val serviciosDisponibles: String? = null,
    @SerializedName("ciudad") val ciudad: String? = null,
    @SerializedName("activo") val activo: Boolean? = true,
    @SerializedName("veterinarios") val veterinarios: List<Veterinario>? = null
)