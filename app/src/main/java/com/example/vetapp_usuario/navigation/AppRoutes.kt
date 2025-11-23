package com.example.vetapp_usuario.navigation

sealed class AppRoutes(val route: String) {

    // ==================== AUTH ====================
    object Login : AppRoutes("login")
    object Register : AppRoutes("register")

    // ==================== HOME ====================
    object Home : AppRoutes("home")

    // ==================== MASCOTAS ====================
    object Mascotas : AppRoutes("mascotas")
    object CrearMascota : AppRoutes("crear_mascota")
    object DetalleMascota : AppRoutes("detalle_mascota/{id}") {
        fun create(id: Int) = "detalle_mascota/$id"
    }

    // ==================== SUCURSALES ====================
    object Sucursales : AppRoutes("sucursales")
    object DetalleSucursal : AppRoutes("detalle_sucursal/{id}") {
        fun create(id: Int) = "detalle_sucursal/$id"
    }

    // ==================== CITAS ====================
    object MisCitas : AppRoutes("mis_citas")

    // ✅ CORREGIDO: Ya no recibe mascotaId
    object CrearCita : AppRoutes("crear_cita")

    object DetalleCita : AppRoutes("detalle_cita/{id}") {
        fun create(id: Int) = "detalle_cita/$id"
    }

    // ==================== RESEÑAS ====================
    object CrearResena : AppRoutes("crear_resena/{citaId}") {
        fun create(citaId: Int) = "crear_resena/$citaId"
    }

    object VerResenas : AppRoutes("ver_resenas/{veterinarioId}") {
        fun create(veterinarioId: Int) = "ver_resenas/$veterinarioId"
    }

    // ==================== SHARED ====================
    object Perfil : AppRoutes("perfil")
    object Settings : AppRoutes("settings")
}