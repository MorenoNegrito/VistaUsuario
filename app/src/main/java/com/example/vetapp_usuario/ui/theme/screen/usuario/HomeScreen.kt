package com.example.vetapp_usuario.ui.theme.screen.usuario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vetapp_usuario.data.local.UsuarioPreferences
import com.example.vetapp_usuario.data.model.MascotaRequest
import com.example.vetapp_usuario.navigation.AppRoutes
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ==================== HOME SCREEN ====================
@ExperimentalMaterial3Api
@Composable
fun HomeUsuarioScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val preferences = remember { UsuarioPreferences(context) }
    val scope = rememberCoroutineScope()

    var userName by remember { mutableStateOf("Usuario") }

    LaunchedEffect(Unit) {
        preferences.userName.first()?.let { userName = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("¡Hola, $userName!") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "¿Qué deseas hacer hoy?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Card Mis Mascotas
            HomeCard(
                title = "Mis Mascotas",
                icon = Icons.Default.FavoriteBorder,
                onClick = { navController.navigate(AppRoutes.Mascotas.route) }
            )

            // Card Agendar Cita
            HomeCard(
                title = "Agendar Cita",
                icon = Icons.Default.DateRange,
                onClick = { navController.navigate(AppRoutes.CrearCita.route) }
            )

            // Card Mis Citas
            HomeCard(
                title = "Mis Citas",
                icon = Icons.Default.DateRange,
                onClick = { navController.navigate(AppRoutes.MisCitas.route) }
            )

            // Card Sucursales
            HomeCard(
                title = "Ver Sucursales",
                icon = Icons.Default.LocationOn,
                onClick = { navController.navigate(AppRoutes.Sucursales.route) }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón cerrar sesión
            OutlinedButton(
                onClick = {
                    scope.launch {
                        preferences.clearAll()
                        navController.navigate(AppRoutes.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AccountBox, null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}

@Composable
private fun HomeCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
