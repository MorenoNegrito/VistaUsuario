package com.example.vetapp_usuario.data.model

data class UsuarioUiState(
    val isLoading: Boolean = false,
    val error: String? = null,

    // Listas
    val mascotas: List<Mascota> = emptyList(),
    val sucursales: List<Sucursal> = emptyList(),
    val veterinarios: List<Veterinario> = emptyList(),
    val citas: List<Cita> = emptyList(),
    val resenas: List<Resena> = emptyList(),

    // Selecciones individuales
    val sucursalSeleccionada: Sucursal? = null,
    val usuario: Usuario? = null,

    // Estado de autenticación
    val isLoggedIn: Boolean = false
)