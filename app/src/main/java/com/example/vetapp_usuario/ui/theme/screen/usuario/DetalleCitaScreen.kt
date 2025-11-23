package com.example.vetapp_usuario.ui.theme.screen.usuario

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
import com.example.vetapp_usuario.data.model.ResenaRequest
import com.example.vetapp_usuario.navigation.AppRoutes
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ==================== CREAR RESEÑA SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCitaScreen(
    navController: NavController,
    viewModel: UsuarioViewModel,
    citaId: Int
) {
    val uiState by viewModel.uiState.collectAsState()
    val cita = uiState.citas.find { it.id == citaId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (cita != null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Estado
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when (cita.estado.uppercase()) {
                            "COMPLETADA" -> MaterialTheme.colorScheme.primaryContainer
                            "PENDIENTE" -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Text(
                        text = "Estado: ${cita.estado}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Información básica
                InfoSection(title = "Información General") {
                    InfoRowDetalle("Mascota", cita.mascota?.nombre ?: "N/A")
                    InfoRowDetalle("Fecha y Hora", formatFecha(cita.fechaHora))
                    InfoRowDetalle("Motivo", cita.motivoCita)
                    cita.mensajeCliente?.let {
                        InfoRowDetalle("Mensaje", it)
                    }
                }

                // Veterinario
                InfoSection(title = "Veterinario") {
                    InfoRowDetalle("Nombre", cita.veterinario?.nombre ?: "N/A")
                    InfoRowDetalle("Especialidad", cita.veterinario?.especialidad ?: "N/A")
                }

                // Sucursal
                InfoSection(title = "Sucursal") {
                    InfoRowDetalle("Nombre", cita.sucursal?.nombre ?: "N/A")
                    InfoRowDetalle("Dirección", cita.sucursal?.direccion ?: "N/A")
                    InfoRowDetalle("Teléfono", cita.sucursal?.telefono ?: "N/A")
                }

                // Resultados (si ya fue atendida)
                if (cita.diagnostico != null || cita.tratamiento != null) {
                    InfoSection(title = "Resultados de la Atención") {
                        cita.diagnostico?.let {
                            InfoRowDetalle("Diagnóstico", it)
                        }
                        cita.tratamiento?.let {
                            InfoRowDetalle("Tratamiento", it)
                        }
                        cita.observaciones?.let {
                            InfoRowDetalle("Observaciones", it)
                        }
                    }
                }

                // Botón dejar reseña si está completada
                if (cita.estado.uppercase() == "COMPLETADA") {
                    Button(
                        onClick = {
                            navController.navigate(AppRoutes.CrearResena.create(cita.id))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Star, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Dejar Reseña")
                    }
                }
            }
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cita no encontrada")
            }
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun InfoRowDetalle(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}