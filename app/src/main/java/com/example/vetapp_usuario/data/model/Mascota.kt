package com.example.vetapp_usuario.data.model

import com.google.gson.annotations.SerializedName


data class Mascota(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("especie") val especie: String,
    @SerializedName("raza") val raza: String,
    @SerializedName("edad") val edad: String,
    @SerializedName("peso") val peso: Double,
    @SerializedName("color") val color: String,
    @SerializedName("vacunas") val vacunas: String?,
    @SerializedName("alergias") val alergias: String?,
    @SerializedName("usuarioId") val usuarioId: Int
)