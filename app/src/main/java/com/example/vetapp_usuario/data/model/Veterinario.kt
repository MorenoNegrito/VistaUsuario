package com.example.vetapp_usuario.data.model
import com.google.gson.annotations.SerializedName

data class Veterinario(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("telefono") val telefono: String,
    @SerializedName("especialidad") val especialidad: String,
    @SerializedName("licencia") val licencia: String,
    @SerializedName("sucursalId") val sucursalId: Int,
    @SerializedName("promResenas") val promResenas: Double? = 0.0,
    @SerializedName("totalResenas") val totalResenas: Int? = 0
)