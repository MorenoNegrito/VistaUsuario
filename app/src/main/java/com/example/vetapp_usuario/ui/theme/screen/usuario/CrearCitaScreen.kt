package com.example.vetapp_usuario.ui.theme.screen.usuario

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vetapp_usuario.data.model.*
import com.example.vetapp_usuario.ui.theme.*
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch
import com.example.vetapp_usuario.ui.theme.components.SelectSucursalDialog
import com.example.vetapp_usuario.ui.theme.components.SelectMascotaDialog
import com.example.vetapp_usuario.ui.theme.components.SelectVeterinarioDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCitaScreen(
    token: String,
    viewModel: UsuarioViewModel,
    onCreated: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    var fechaHora by remember { mutableStateOf("") }
    var motivoCita by remember { mutableStateOf("") }
    var mensajeCliente by remember { mutableStateOf("") }

    // Dialogos
    var showMascotaDialog by remember { mutableStateOf(false) }
    var showSucursalDialog by remember { mutableStateOf(false) }
    var showVeterinarioDialog by remember { mutableStateOf(false) }

    // Cargar datos iniciales
    LaunchedEffect(token) {
        viewModel.loadMascotas(token)
        viewModel.loadSucursales()
    }

    LaunchedEffect(uiState.sucursalSeleccionada?.id) {
        uiState.sucursalSeleccionada?.let { sucursal ->
            viewModel.loadVeterinariosBySucursal(sucursal.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Cita", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onCreated) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundWhite,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text("Información de la Cita", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    // Mascota
                    SelectionCard(
                        icon = Icons.Default.Pets,
                        label = "Mascota",
                        value = uiState.mascotaSeleccionada?.nombre ?: "Seleccionar mascota",
                        isSelected = uiState.mascotaSeleccionada != null,
                        onClick = { showMascotaDialog = true }
                    )

                    Divider(color = BorderLight)

                    // Sucursal
                    SelectionCard(
                        icon = Icons.Default.LocationOn,
                        label = "Sucursal",
                        value = uiState.sucursalSeleccionada?.nombre ?: "Seleccionar sucursal",
                        isSelected = uiState.sucursalSeleccionada != null,
                        onClick = { showSucursalDialog = true }
                    )

                    Divider(color = BorderLight)

                    // Veterinario
                    SelectionCard(
                        icon = Icons.Default.Person,
                        label = "Veterinario",
                        value = uiState.veterinarioSeleccionado?.nombre?.let { "Dr. $it" } ?: "Seleccionar veterinario",
                        isSelected = uiState.veterinarioSeleccionado != null,
                        enabled = uiState.sucursalSeleccionada != null,
                        onClick = { if (uiState.sucursalSeleccionada != null) showVeterinarioDialog = true }
                    )

                    if (uiState.sucursalSeleccionada == null) {
                        Text("* Selecciona primero una sucursal", fontSize = 12.sp, color = AccentOrange, modifier = Modifier.padding(start = 36.dp))
                    }
                }
            }

            Text("Detalles", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    OutlinedTextField(
                        value = fechaHora,
                        onValueChange = { fechaHora = it },
                        label = { Text("Fecha y Hora") },
                        placeholder = { Text("2024-12-25 10:30:00") },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = PrimaryBlue) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            focusedLabelColor = PrimaryBlue,
                            cursorColor = PrimaryBlue
                        )
                    )

                    OutlinedTextField(
                        value = motivoCita,
                        onValueChange = { motivoCita = it },
                        label = { Text("Motivo de la Cita") },
                        placeholder = { Text("Consulta general, vacunación, etc.") },
                        leadingIcon = { Icon(Icons.Default.MedicalServices, null, tint = PrimaryBlue) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            focusedLabelColor = PrimaryBlue,
                            cursorColor = PrimaryBlue
                        )
                    )

                    OutlinedTextField(
                        value = mensajeCliente,
                        onValueChange = { mensajeCliente = it },
                        label = { Text("Mensaje Adicional (opcional)") },
                        placeholder = { Text("Información adicional...") },
                        leadingIcon = { Icon(Icons.Default.Notes, null, tint = PrimaryBlue) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            focusedLabelColor = PrimaryBlue,
                            cursorColor = PrimaryBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    scope.launch {
                        val request = CitaRequest(
                            mascotaId = uiState.mascotaSeleccionada?.id ?: 0,
                            sucursalId = uiState.sucursalSeleccionada?.id ?: 0,
                            veterinarioId = uiState.veterinarioSeleccionado?.id ?: 0,
                            fechaHora = fechaHora,
                            motivoCita = motivoCita,
                            mensajeCliente = mensajeCliente.ifBlank { null }
                        )

                        viewModel.crearCita(token, request) {
                            Toast.makeText(context, "Cita agendada con éxito", Toast.LENGTH_LONG).show()
                            onCreated()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                enabled = !uiState.isLoading &&
                        fechaHora.isNotBlank() &&
                        motivoCita.isNotBlank() &&
                        uiState.mascotaSeleccionada != null &&
                        uiState.sucursalSeleccionada != null &&
                        uiState.veterinarioSeleccionado != null,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    disabledContainerColor = BorderLight
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agendar Cita", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }

    if (showMascotaDialog) SelectMascotaDialog(uiState.mascotas, onDismiss = { showMascotaDialog = false }) { mascota ->
        viewModel.seleccionarMascota(mascota)
        showMascotaDialog = false
    }

    if (showSucursalDialog) SelectSucursalDialog(uiState.sucursales, onDismiss = { showSucursalDialog = false }) { sucursal ->
        viewModel.seleccionarSucursal(sucursal)
        viewModel.seleccionarVeterinario(null)
        showSucursalDialog = false
    }

    if (showVeterinarioDialog) SelectVeterinarioDialog(uiState.veterinarios, onDismiss = { showVeterinarioDialog = false }) { vet ->
        viewModel.seleccionarVeterinario(vet)
        showVeterinarioDialog = false
    }
}

// ---------------------- COMPONENTES ----------------------

@Composable
fun SelectionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isSelected: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .alpha(if (enabled) 1f else 0.5f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = if (isSelected) PrimaryBlue else TextSecondary)
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 15.sp, color = if (isSelected) TextPrimary else TextSecondary, fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal)
        }
        Icon(if (isSelected) Icons.Default.CheckCircle else Icons.Default.ChevronRight, contentDescription = null, tint = if (isSelected) AccentGreen else TextTertiary)
    }
}
