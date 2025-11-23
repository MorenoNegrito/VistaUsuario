package com.example.vetapp_usuario.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vetapp_usuario.ui.theme.screen.auth.LoginScreen
import com.example.vetapp_usuario.ui.theme.screen.auth.RegisterScreen

import com.example.vetapp_usuario.ui.theme.screen.usuario.HomeUsuarioScreen
import com.example.vetapp_usuario.ui.theme.screen.usuario.MascotasScreen
import com.example.vetapp_usuario.ui.theme.screen.usuario.CrearMascotaScreen
import com.example.vetapp_usuario.ui.theme.screen.usuario.DetalleMascotaScreen
import com.example.vetapp_usuario.ui.theme.screen.usuario.SucursalesScreen
import com.example.vetapp_usuario.ui.theme.screen.usuario.DetalleSucursalScreen
import com.example.vetapp_usuario.ui.theme.screen.usuario.MisCitasScreen
import com.example.vetapp_usuario.ui.theme.screen.usuario.CrearCitaScreen
import com.example.vetapp_usuario.ui.theme.screen.usuario.DetalleCitaScreen
import com.example.vetapp_usuario.ui.theme.screen.usuario.CrearResenaScreen
import com.example.vetapp_usuario.ui.theme.screen.usuario.VerResenasScreen

import com.example.vetapp_usuario.ui.theme.screen.shared.ProfileScreen
import com.example.vetapp_usuario.ui.theme.screen.shared.SettingsScreen
import com.example.vetapp_usuario.viewmodel.AuthViewModel
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    usuarioViewModel: UsuarioViewModel
) {

    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {
        // ==================== AUTH ====================
        composable(AppRoutes.Login.route) {
            LoginScreen(navController, authViewModel)
        }

        composable(AppRoutes.Register.route) {
            RegisterScreen(navController, authViewModel)
        }

        // ==================== HOME ====================
        composable(AppRoutes.Home.route) {
            HomeUsuarioScreen(navController, usuarioViewModel)
        }

        // ==================== MASCOTAS ====================
        composable(AppRoutes.Mascotas.route) {
            MascotasScreen(navController, usuarioViewModel)
        }

        composable(AppRoutes.CrearMascota.route) {
            CrearMascotaScreen(navController, usuarioViewModel)
        }

        composable(AppRoutes.DetalleMascota.route) {
            val id = it.arguments?.getString("id")?.toIntOrNull() ?: 0
            DetalleMascotaScreen(navController, usuarioViewModel, id)
        }

        // ==================== SUCURSALES ====================
        composable(AppRoutes.Sucursales.route) {
            SucursalesScreen(navController, usuarioViewModel)
        }

        composable(AppRoutes.DetalleSucursal.route) {
            val id = it.arguments?.getString("id")?.toIntOrNull() ?: 0
            DetalleSucursalScreen(navController, usuarioViewModel, id)
        }

        // ==================== CITAS ====================
        composable(AppRoutes.MisCitas.route) {
            MisCitasScreen(navController, usuarioViewModel)
        }

        composable(AppRoutes.CrearCita.route) {
            // ✅ CORREGIDO: Ya no recibe mascotaId, carga todo dentro
            CrearCitaScreen(navController, usuarioViewModel)
        }

        composable(AppRoutes.DetalleCita.route) {
            val id = it.arguments?.getString("id")?.toIntOrNull() ?: 0
            DetalleCitaScreen(navController, usuarioViewModel, id)
        }

        // ==================== RESEÑAS ====================
        composable(AppRoutes.CrearResena.route) {
            val citaId = it.arguments?.getString("citaId")?.toIntOrNull() ?: 0
            CrearResenaScreen(navController, usuarioViewModel, citaId)
        }

        composable(AppRoutes.VerResenas.route) {
            val veterinarioId = it.arguments?.getString("veterinarioId")?.toIntOrNull() ?: 0
            VerResenasScreen(navController, usuarioViewModel, veterinarioId)
        }

        // ==================== SHARED ====================
        composable(AppRoutes.Perfil.route) {
            ProfileScreen(navController)
        }

        composable(AppRoutes.Settings.route) {
            SettingsScreen(navController)
        }
    }
}