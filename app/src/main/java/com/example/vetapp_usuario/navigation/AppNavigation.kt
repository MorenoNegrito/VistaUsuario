package com.example.vetapp_usuario.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                preferences = preferences, // 🔥 Agregado
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
                preferences = preferences, // 🔥 Agregado
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
                onResenasClick = {},
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
                token = token ?: "",
                onCrearMascota = {
                    navController.navigate(AppRoutes.CrearMascota.route)
                }
            )
        }

        composable(AppRoutes.CrearMascota.route) {
            val token by preferences.token.collectAsState(initial = null)
            CrearMascotaScreen(
                token = token ?: "",
                viewModel = usuarioViewModel,
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.DetalleMascota.route) {
            val id = it.arguments?.getString("id")?.toIntOrNull() ?: 0
            DetalleMascotaScreen(
                mascotaId = id,
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

        composable(AppRoutes.DetalleSucursal.route) {
            val id = it.arguments?.getString("sucursalId")?.toIntOrNull() ?: 0
            DetalleSucursalScreen(
                sucursalId = id,
                viewModel = usuarioViewModel,
                navController = navController
            )
        }

        // ==================== CITAS ====================
        composable(AppRoutes.MisCitas.route) {
            MisCitasScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }

        composable(AppRoutes.CrearCita.route) {
            val token by preferences.token.collectAsState(initial = null)
            CrearCitaScreen(
                token = token ?: "", // aquí se asegura que siempre sea String NO nulo
                viewModel = usuarioViewModel,
                onCreated = { navController.popBackStack() }
            )

        }


        composable(AppRoutes.DetalleCita.route) { backStack ->
            val token by preferences.token.collectAsState(initial = null)
            val citaId = backStack.arguments?.getString("id")?.toInt() ?: 0
            DetalleCitaScreen(
                token = token ?: "",
                citaId = citaId,
                viewModel = usuarioViewModel,
                onCancelSuccess = { navController.popBackStack() }
            )
        }

        // ==================== RESEÑAS ====================
        composable(AppRoutes.CrearResena.route) { backStack ->
            val token by preferences.token.collectAsState(initial = null)
            val citaId = backStack.arguments?.getString("citaId")?.toInt() ?: 0
            val vetId = usuarioViewModel.uiState.value.veterinarioSeleccionado?.id ?: 0

            CrearResenaScreen(
                token = token ?: "",
                citaId = citaId,
                veterinarioId = vetId,
                viewModel = usuarioViewModel,
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.VerResenas.route) { backStack ->
            val veterinarioId = backStack.arguments?.getString("veterinarioId")?.toIntOrNull() ?: 0
            VerResenasScreen(
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