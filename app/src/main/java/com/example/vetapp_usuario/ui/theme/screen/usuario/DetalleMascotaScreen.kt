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
fun DetalleMascotaScreen(
    navController: NavController,
    viewModel: UsuarioViewModel,
    mascotaId: Int
) {
    val uiState by viewModel.uiState.collectAsState()
    val mascota = uiState.mascotas.find { it.id == mascotaId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(mascota?.nombre ?: "Detalle Mascota") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (mascota != null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DetailRow("Nombre:", mascota.nombre)
                DetailRow("Especie:", mascota.especie)
                DetailRow("Raza:", mascota.raza)
                DetailRow("Edad:", mascota.edad)
                DetailRow("Peso:", "${mascota.peso} kg")
                DetailRow("Color:", mascota.color)
                DetailRow("Vacunas:", mascota.vacunas ?: "N/A")
                DetailRow("Alergias:", mascota.alergias ?: "Ninguna")

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate(AppRoutes.CrearCita.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agendar Cita para ${mascota.nombre}")
                }
            }
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Mascota no encontrada")
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.6f)
        )
    }
}