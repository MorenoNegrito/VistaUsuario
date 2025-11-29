package com.example.vetapp_usuario.data.model

import com.google.gson.annotations.SerializedName

data class Resena(
    @SerializedName("id")
    val id: Int,

    @SerializedName("estrellas")
    val estrellas: Int,

    @SerializedName("comentario")
    val comentario: String,

    @SerializedName("fechaCreacion")
    val fechaCreacion: String,

    @SerializedName("usuario")
    val usuario: UsuarioBasico? = null,

    @SerializedName("veterinario")
    val veterinario: VeterinarioBasico? = null,

    @SerializedName("cita")
    val cita: CitaBasico? = null
)


data class UsuarioBasico(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("apellido")
    val apellido: String? = null
)


data class VeterinarioBasico(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("especialidad")
    val especialidad: String? = null
)


data class CitaBasico(
    @SerializedName("id")
    val id: Int,

    @SerializedName("fechaHora")
    val fechaHora: String? = null
)