package com.example.vetapp_usuario.data.model


import com.google.gson.annotations.SerializedName

data class MascotaRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("especie") val especie: String,
    @SerializedName("raza") val raza: String,
    @SerializedName("edad") val edad: String,
    @SerializedName("peso") val peso: Double,
    @SerializedName("color") val color: String,
    @SerializedName("vacunas") val vacunas: String?,
    @SerializedName("alergias") val alergias: String?
)
