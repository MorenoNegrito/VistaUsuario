package com.example.vetapp_usuario.ui.theme.screen.usuario


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun DetalleSucursalScreen(
    navController: NavController,
    viewModel: UsuarioViewModel,
    sucursalId: Int
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(sucursalId) {
        viewModel.loadSucursalDetail(sucursalId)
    }

    val sucursal = uiState.sucursalSeleccionada

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sucursal?.nombre ?: "Detalle Sucursal") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                sucursal == null -> {
                    Text(
                        text = "No se encontró la sucursal",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // Información de la sucursal
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Información",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(12.dp))

                                InfoRow(Icons.Default.Place, "Dirección", sucursal.direccion)
                                InfoRow(Icons.Default.LocationOn, "Ciudad", sucursal.ciudad)
                                InfoRow(Icons.Default.Phone, "Teléfono", sucursal.telefono)
                                InfoRow(Icons.Default.DateRange, "Horario", sucursal.horarioAtencion)
                                InfoRow(Icons.Default.Star, "Servicios", sucursal.serviciosDisponibles)
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        // Lista de veterinarios
                        Text(
                            text = "Veterinarios Disponibles",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(12.dp))

                        if (uiState.veterinarios.isEmpty()) {
                            Text(
                                text = "No hay veterinarios registrados en esta sucursal",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            uiState.veterinarios.forEach { vet ->
                                VeterinarioCard(veterinario = vet)
                                Spacer(Modifier.height(12.dp))
                            }
                        }

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = { navController.navigate(AppRoutes.CrearCita.route) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.DateRange, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Agendar Cita en esta Sucursal")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun VeterinarioCard(
    veterinario: com.example.vetapp_usuario.data.model.Veterinario
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = veterinario.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = veterinario.especialidad,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Lic: ${veterinario.licencia}",
                    style = MaterialTheme.typography.bodySmall
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${veterinario.promResenas ?: 0.0} (${veterinario.totalResenas ?: 0} reseñas)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}