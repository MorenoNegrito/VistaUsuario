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
    object DetalleMascota : AppRoutes("detalle_mascota/{mascotaId}") {
        fun create(mascotaId: Int) = "detalle_mascota/$mascotaId"
    }

    // ==================== SUCURSALES ====================
    object Sucursales : AppRoutes("sucursales")
    object DetalleSucursal : AppRoutes("detalle_sucursal/{sucursalId}") {
        fun create(sucursalId: Int) = "detalle_sucursal/$sucursalId"
    }

    // ==================== CITAS ====================
    object MisCitas : AppRoutes("mis_citas")
    object CrearCita : AppRoutes("crear_cita")
    object DetalleCita : AppRoutes("detalle_cita/{citaId}") {
        fun create(citaId: Int) = "detalle_cita/$citaId"
    }

    // ==================== RESEÑAS ====================
    object CrearResena : AppRoutes("crear_resena/{citaId}/{veterinarioId}") {
        fun create(citaId: Int, veterinarioId: Int) = "crear_resena/$citaId/$veterinarioId"
    }

    object VerResenas : AppRoutes("ver_resenas/{veterinarioId}") {
        fun create(veterinarioId: Int) = "ver_resenas/$veterinarioId"
    }

    // ==================== SHARED ====================
    object Perfil : AppRoutes("perfil")
    object Settings : AppRoutes("settings")
}
