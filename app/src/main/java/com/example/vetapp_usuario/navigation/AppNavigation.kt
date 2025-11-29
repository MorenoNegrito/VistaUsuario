package com.example.vetapp_usuario.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vetapp_usuario.data.local.UsuarioPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// AUTH
import com.example.vetapp_usuario.ui.theme.screen.auth.LoginScreen
import com.example.vetapp_usuario.ui.theme.screen.auth.RegisterScreen

// USUARIO SCREENS
import com.example.vetapp_usuario.ui.theme.screen.usuario.*

import com.example.vetapp_usuario.ui.theme.screen.shared.ProfileScreen
import com.example.vetapp_usuario.ui.theme.screen.shared.SettingsScreen

// VIEWMODELS
import com.example.vetapp_usuario.viewmodel.AuthViewModel
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    usuarioViewModel: UsuarioViewModel,
    preferences: UsuarioPreferences
) {

    // Verificar si hay sesión activa al iniciar
    val scope = rememberCoroutineScope()
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            val isLoggedIn = preferences.isLoggedIn.first()
            startDestination = if (isLoggedIn) {
                AppRoutes.Home.route
            } else {
                AppRoutes.Login.route
            }
        }
    }

    // Esperar a que se determine el destino inicial
    if (startDestination == null) {
        // Mostrar splash o loading mientras se verifica
        return
    }

    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {

        // ==================== AUTH ====================
        composable(AppRoutes.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                preferences = preferences,
                onLoginSuccess = {
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                },
                onGoRegister = {
                    navController.navigate(AppRoutes.Register.route)
                }
            )
        }

        composable(AppRoutes.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,
                preferences = preferences,
                onRegisterSuccess = {
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Register.route) { inclusive = true }
                    }
                },
                onGoLogin = {
                    navController.navigate(AppRoutes.Login.route)
                }
            )
        }

        // ==================== HOME ====================
        composable(AppRoutes.Home.route) {
            HomeScreen(
                onMascotasClick = { navController.navigate(AppRoutes.Mascotas.route) },
                onSucursalesClick = { navController.navigate(AppRoutes.Sucursales.route) },
                onCitasClick = { navController.navigate(AppRoutes.MisCitas.route) },
                onResenasClick = { navController.navigate(AppRoutes.Sucursales.route) },  // ✅ Navegar a Sucursales
                onCrearMascotaClick = { navController.navigate(AppRoutes.CrearMascota.route) },
                onCrearCitaClick = { navController.navigate(AppRoutes.CrearCita.route) },
                onProfileClick = { navController.navigate(AppRoutes.Settings.route) }
            )
        }

        // ==================== MASCOTAS ====================
        composable(AppRoutes.Mascotas.route) {
            val token by preferences.token.collectAsState(initial = null)
            MascotasScreen(
                viewModel = usuarioViewModel,
                token = token ?: "",  // ✅ Usa "" si token es null
                onCrearMascota = {
                    navController.navigate(AppRoutes.CrearMascota.route)
                }
            )
        }

        composable(AppRoutes.CrearMascota.route) {
            val token by preferences.token.collectAsState(initial = null)
            CrearMascotaScreen(
                token = token ?: "",  // ✅ Usa "" si token es null
                viewModel = usuarioViewModel,
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = "detalle_mascota/{mascotaId}",
            arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
        ) { backStack ->
            val mascotaId = backStack.arguments?.getInt("mascotaId") ?: 0
            DetalleMascotaScreen(
                mascotaId = mascotaId,
                viewModel = usuarioViewModel,
                navController = navController
            )
        }

        // ==================== SUCURSALES ====================
        composable(AppRoutes.Sucursales.route) {
            SucursalesScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }

        composable(
            route = "detalle_sucursal/{sucursalId}",
            arguments = listOf(navArgument("sucursalId") { type = NavType.IntType })
        ) { backStack ->
            val sucursalId = backStack.arguments?.getInt("sucursalId") ?: 0
            DetalleSucursalScreen(
                sucursalId = sucursalId,
                viewModel = usuarioViewModel,
                navController = navController
            )
        }

        // ==================== CITAS ====================
        composable(AppRoutes.MisCitas.route) {
            val token by preferences.token.collectAsState(initial = null)
            MisCitasScreen(
                token = token ?: "",  // ✅ Usa "" si token es null
                navController = navController,
                viewModel = usuarioViewModel
            )
        }

        composable(AppRoutes.CrearCita.route) {
            val token by preferences.token.collectAsState(initial = null)
            CrearCitaScreen(
                token = token ?: "",  // ✅ Usa "" si token es null
                viewModel = usuarioViewModel,
                onCreated = { navController.popBackStack() }
            )
        }

        composable(
            route = "detalle_cita/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStack ->
            val token by preferences.token.collectAsState(initial = "")
            val citaId = backStack.arguments?.getInt("citaId") ?: 0
            DetalleCitaScreen(
                token = token ?: "",  // ✅ Manejar nullable
                citaId = citaId,
                viewModel = usuarioViewModel,
                navController = navController,
                onCancelSuccess = { navController.popBackStack() }
            )
        }

        // ==================== RESEÑAS ====================
        // Crear reseña (solo necesita veterinarioId)
        composable(
            route = "crear_resena/{veterinarioId}",
            arguments = listOf(
                navArgument("veterinarioId") { type = NavType.IntType }
            )
        ) { backStack ->
            val token by preferences.token.collectAsState(initial = "")
            val veterinarioId = backStack.arguments?.getInt("veterinarioId") ?: 0

            CrearResenaScreen(
                token = token ?: "",
                veterinarioId = veterinarioId,
                viewModel = usuarioViewModel,
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = "ver_resenas/{veterinarioId}",
            arguments = listOf(navArgument("veterinarioId") { type = NavType.IntType })
        ) { backStack ->
            val token by preferences.token.collectAsState(initial = "")  // ✅ Agregar token
            val veterinarioId = backStack.arguments?.getInt("veterinarioId") ?: 0
            VerResenasScreen(
                token = token ?: "",  // ✅ Pasar token
                veterinarioId = veterinarioId,
                viewModel = usuarioViewModel,
                navController = navController
            )
        }

        // ==================== AJUSTES ====================
        composable(AppRoutes.Perfil.route) {
            ProfileScreen(
                navController = navController,
                preferences = preferences
            )
        }

        composable(AppRoutes.Settings.route) {
            SettingsScreen(
                navController = navController,
                prefs = preferences,
                onLogout = {
                    scope.launch {
                        preferences.clearAll()
                        navController.navigate(AppRoutes.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}