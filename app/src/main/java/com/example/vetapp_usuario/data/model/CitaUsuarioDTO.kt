package com.example.vetapp_usuario.data.model

import com.google.gson.annotations.SerializedName

// DTO que devuelve el backend en GET /api/citas
data class CitaUsuarioDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("fechaHora")
    val fechaHora: String,

    @SerializedName("motivo")  // ← Backend usa "motivo", no "motivoCita"
    val motivo: String,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("mascotaNombre")
    val mascotaNombre: String,

    @SerializedName("veterinarioNombre")
    val veterinarioNombre: String,

    @SerializedName("veterinarioEspecialidad")
    val veterinarioEspecialidad: String?,

    @SerializedName("veterinarioId")
    val veterinarioId: Long? = null,

    @SerializedName("sucursalNombre")
    val sucursalNombre: String
)