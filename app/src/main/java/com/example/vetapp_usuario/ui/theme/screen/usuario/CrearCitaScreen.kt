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
import com.example.vetapp_usuario.data.model.CitaRequest
import com.example.vetapp_usuario.navigation.AppRoutes
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCitaScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current
    val preferences = remember { UsuarioPreferences(context) }
    val scope = rememberCoroutineScope()

    var selectedMascotaId by remember { mutableStateOf<Int?>(null) }
    var selectedSucursalId by remember { mutableStateOf<Int?>(null) }
    var selectedVeterinarioId by remember { mutableStateOf<Int?>(null) }
    var motivo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // Fecha y hora seleccionadas
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    // Estados de expansión
    var mascotaExpanded by remember { mutableStateOf(false) }
    var sucursalExpanded by remember { mutableStateOf(false) }
    var veterinarioExpanded by remember { mutableStateOf(false) }

    // Cargar datos iniciales
    LaunchedEffect(Unit) {
        val token = preferences.token.first()
        token?.let {
            viewModel.loadMascotas(it)
            viewModel.loadSucursales()
        }
    }

    // Cargar veterinarios cuando se seleccione sucursal
    LaunchedEffect(selectedSucursalId) {
        selectedSucursalId?.let {
            viewModel.loadVeterinariosBySucursal(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Información de la Cita",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Selector de Mascota
            ExposedDropdownMenuBox(
                expanded = mascotaExpanded,
                onExpandedChange = { mascotaExpanded = !mascotaExpanded }
            ) {
                OutlinedTextField(
                    value = uiState.mascotas.find { it.id == selectedMascotaId }?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecciona tu mascota") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = mascotaExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    placeholder = { Text("Elige una mascota") }
                )
                ExposedDropdownMenu(
                    expanded = mascotaExpanded,
                    onDismissRequest = { mascotaExpanded = false }
                ) {
                    uiState.mascotas.forEach { mascota ->
                        DropdownMenuItem(
                            text = { Text("${mascota.nombre} (${mascota.especie})") },
                            onClick = {
                                selectedMascotaId = mascota.id
                                mascotaExpanded = false
                            }
                        )
                    }
                }
            }

            // Selector de Sucursal
            ExposedDropdownMenuBox(
                expanded = sucursalExpanded,
                onExpandedChange = { sucursalExpanded = !sucursalExpanded }
            ) {
                OutlinedTextField(
                    value = uiState.sucursales.find { it.id == selectedSucursalId }?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecciona sucursal") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sucursalExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    placeholder = { Text("Elige una sucursal") }
                )
                ExposedDropdownMenu(
                    expanded = sucursalExpanded,
                    onDismissRequest = { sucursalExpanded = false }
                ) {
                    uiState.sucursales.forEach { sucursal ->
                        DropdownMenuItem(
                            text = { Text("${sucursal.nombre} - ${sucursal.ciudad}") },
                            onClick = {
                                selectedSucursalId = sucursal.id
                                selectedVeterinarioId = null // Reset veterinario
                                sucursalExpanded = false
                            }
                        )
                    }
                }
            }

            // Selector de Veterinario
            ExposedDropdownMenuBox(
                expanded = veterinarioExpanded,
                onExpandedChange = {
                    if (selectedSucursalId != null) veterinarioExpanded = !veterinarioExpanded
                }
            ) {
                OutlinedTextField(
                    value = uiState.veterinarios.find { it.id == selectedVeterinarioId }?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecciona veterinario") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = veterinarioExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    placeholder = { Text("Primero selecciona sucursal") },
                    enabled = selectedSucursalId != null
                )
                ExposedDropdownMenu(
                    expanded = veterinarioExpanded,
                    onDismissRequest = { veterinarioExpanded = false }
                ) {
                    uiState.veterinarios.forEach { vet ->
                        DropdownMenuItem(
                            text = { Text("${vet.nombre} - ${vet.especialidad}") },
                            onClick = {
                                selectedVeterinarioId = vet.id
                                veterinarioExpanded = false
                            }
                        )
                    }
                }
            }

            // Fecha y Hora (inputs simples por ahora)
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { selectedDate = it },
                label = { Text("Fecha (YYYY-MM-DD)") },
                placeholder = { Text("2025-12-25") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = selectedTime,
                onValueChange = { selectedTime = it },
                label = { Text("Hora (HH:MM)") },
                placeholder = { Text("14:30") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo de la cita") },
                placeholder = { Text("Ej: Control rutinario") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                label = { Text("Mensaje adicional (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    scope.launch {
                        val token = preferences.token.first()
                        if (token != null && selectedMascotaId != null &&
                            selectedSucursalId != null && selectedVeterinarioId != null &&
                            selectedDate.isNotBlank() && selectedTime.isNotBlank()) {

                            val fechaHora = "${selectedDate}T${selectedTime}:00"

                            viewModel.crearCita(
                                token = token,
                                request = CitaRequest(
                                    mascotaId = selectedMascotaId!!,
                                    sucursalId = selectedSucursalId!!,
                                    veterinarioId = selectedVeterinarioId!!,
                                    fechaHora = fechaHora,
                                    motivoCita = motivo,
                                    mensajeCliente = mensaje.ifEmpty { null }
                                ),
                                onSuccess = { navController.popBackStack() }
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedMascotaId != null && selectedSucursalId != null &&
                        selectedVeterinarioId != null && selectedDate.isNotBlank() &&
                        selectedTime.isNotBlank() && motivo.isNotBlank()
            ) {
                Icon(Icons.Default.Check, null)
                Spacer(Modifier.width(8.dp))
                Text("Agendar Cita")
            }
        }
    }
}