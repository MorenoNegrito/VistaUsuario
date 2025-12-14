package com.example.vetapp_usuario.ui.theme.screen.usuario

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import java.text.SimpleDateFormat
import java.util.*


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

    // Estados para fecha y hora usando Calendar
    val calendar = remember { Calendar.getInstance() }
    var selectedDateText by remember { mutableStateOf("") }
    var selectedTimeText by remember { mutableStateOf("") }
    var fechaHoraISO by remember { mutableStateOf("") }

    var motivoCita by remember { mutableStateOf("") }
    var mensajeCliente by remember { mutableStateOf("") }

    // Dialogos de selección
    var showMascotaDialog by remember { mutableStateOf(false) }
    var showSucursalDialog by remember { mutableStateOf(false) }
    var showVeterinarioDialog by remember { mutableStateOf(false) }

    // Formatters
    val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val displayTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

     LaunchedEffect(Unit) {
          viewModel.loadSucursales()
      }

        // Cargar mascotas cuando el token esté listo
        LaunchedEffect(token) {
            if (token.isNotBlank()) {
               viewModel.loadMascotas(token)
           }
        }

       // Validar si hay mascotas después de cargar
        LaunchedEffect(uiState.mascotas, uiState.isLoading) {
            if (!uiState.isLoading && uiState.mascotas.isEmpty() && token.isNotBlank()) {
                Toast.makeText(
                    context,
                    "Debes crear una mascota primero antes de agendar una cita",
                    Toast.LENGTH_LONG
               ).show()
            }
        }

    LaunchedEffect(uiState.sucursalSeleccionada?.id) {
        uiState.sucursalSeleccionada?.let { sucursal ->
            viewModel.loadVeterinariosBySucursal(sucursal.id)
        }
    }

    // Mostrar errores
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            viewModel.clearError()
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

            Text(
                "Información de la Cita",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

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
                        value = uiState.veterinarioSeleccionado?.nombre?.let { "Dr. $it" }
                            ?: "Seleccionar veterinario",
                        isSelected = uiState.veterinarioSeleccionado != null,
                        enabled = uiState.sucursalSeleccionada != null,
                        onClick = {
                            if (uiState.sucursalSeleccionada != null) showVeterinarioDialog = true
                        }
                    )

                    if (uiState.sucursalSeleccionada == null) {
                        Text(
                            "* Selecciona primero una sucursal",
                            fontSize = 12.sp,
                            color = AccentOrange,
                            modifier = Modifier.padding(start = 36.dp)
                        )
                    }
                }
            }

            Text(
                "Fecha y Hora",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // Campo de Fecha
                    OutlinedTextField(
                        value = selectedDateText,
                        onValueChange = {},
                        label = { Text("Fecha") },
                        placeholder = { Text("Seleccionar fecha") },
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = PrimaryBlue
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, day ->
                                            calendar.set(year, month, day)
                                            selectedDateText = displayDateFormat.format(calendar.time)
                                            // Actualizar ISO si también hay hora
                                            if (selectedTimeText.isNotEmpty()) {
                                                fechaHoraISO = isoFormat.format(calendar.time)
                                            }
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                            ) {
                                Icon(
                                    Icons.Default.EditCalendar,
                                    contentDescription = "Seleccionar fecha",
                                    tint = PrimaryBlue
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = BorderMedium,
                            cursorColor = PrimaryBlue
                        )
                    )

                    // Campo de Hora
                    OutlinedTextField(
                        value = selectedTimeText,
                        onValueChange = {},
                        label = { Text("Hora") },
                        placeholder = { Text("Seleccionar hora") },
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                tint = PrimaryBlue
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    TimePickerDialog(
                                        context,
                                        { _, hour, minute ->
                                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                                            calendar.set(Calendar.MINUTE, minute)
                                            calendar.set(Calendar.SECOND, 0)
                                            selectedTimeText = displayTimeFormat.format(calendar.time)
                                            // Actualizar ISO si también hay fecha
                                            if (selectedDateText.isNotEmpty()) {
                                                fechaHoraISO = isoFormat.format(calendar.time)
                                            }
                                        },
                                        calendar.get(Calendar.HOUR_OF_DAY),
                                        calendar.get(Calendar.MINUTE),
                                        true // formato 24 horas
                                    ).show()
                                }
                            ) {
                                Icon(
                                    Icons.Default.AccessTime,
                                    contentDescription = "Seleccionar hora",
                                    tint = PrimaryBlue
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = BorderMedium,
                            cursorColor = PrimaryBlue
                        )
                    )

                    // Mostrar formato ISO cuando ambos estén seleccionados
                    if (selectedDateText.isNotEmpty() && selectedTimeText.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF0FDF4)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = AccentGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Formato: $fechaHoraISO",
                                    fontSize = 12.sp,
                                    color = Color(0xFF166534),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Text(
                "Detalles de la Consulta",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedTextField(
                        value = motivoCita,
                        onValueChange = { motivoCita = it },
                        label = { Text("Motivo de la Cita") },
                        placeholder = { Text("Ej: Control rutinario, vacunación, consulta...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = PrimaryBlue
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = BorderMedium,
                            cursorColor = PrimaryBlue
                        )
                    )

                    OutlinedTextField(
                        value = mensajeCliente,
                        onValueChange = { mensajeCliente = it },
                        label = { Text("Mensaje Adicional (opcional)") },
                        placeholder = { Text("Información adicional que el veterinario deba saber...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Notes,
                                contentDescription = null,
                                tint = PrimaryBlue
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = BorderMedium,
                            cursorColor = PrimaryBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Agendar
            Button(
                onClick = {
                    // Validaciones
                    if (token.isBlank()) {
                        Toast.makeText(
                            context,
                            "Error: Sesión no válida",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val mascotaId = uiState.mascotaSeleccionada?.id
                    val sucursalId = uiState.sucursalSeleccionada?.id
                    val veterinarioId = uiState.veterinarioSeleccionado?.id

                    if (mascotaId == null) {
                        Toast.makeText(
                            context,
                            "Por favor selecciona una mascota",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (sucursalId == null) {
                        Toast.makeText(
                            context,
                            "Por favor selecciona una sucursal",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (veterinarioId == null) {
                        Toast.makeText(
                            context,
                            "Por favor selecciona un veterinario",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (selectedDateText.isEmpty() || selectedTimeText.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Por favor selecciona fecha y hora",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (motivoCita.isBlank()) {
                        Toast.makeText(
                            context,
                            "Por favor indica el motivo de la cita",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    scope.launch {
                        val request = CitaRequest(
                            mascotaId = mascotaId,
                            sucursalId = sucursalId,
                            veterinarioId = veterinarioId,
                            fechaHora = fechaHoraISO,
                            motivoCita = motivoCita,
                            mensajeCliente = mensajeCliente.ifBlank { null }
                        )

                        viewModel.crearCita(token, request) {
                            Toast.makeText(
                                context,
                                "✅ Cita agendada con éxito",
                                Toast.LENGTH_LONG
                            ).show()
                            onCreated()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = !uiState.isLoading &&
                        token.isNotBlank() &&
                        selectedDateText.isNotEmpty() &&
                        selectedTimeText.isNotEmpty() &&
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
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agendar Cita", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Diálogos de selección
    if (showMascotaDialog) {
        SelectMascotaDialog(
            mascotas = uiState.mascotas,
            onDismiss = { showMascotaDialog = false }
        ) { mascota ->
            viewModel.seleccionarMascota(mascota)
            showMascotaDialog = false
        }
    }

    if (showSucursalDialog) {
        SelectSucursalDialog(
            sucursales = uiState.sucursales,
            onDismiss = { showSucursalDialog = false }
        ) { sucursal ->
            viewModel.seleccionarSucursal(sucursal)
            viewModel.seleccionarVeterinario(null)
            showSucursalDialog = false
        }
    }

    if (showVeterinarioDialog) {
        SelectVeterinarioDialog(
            veterinarios = uiState.veterinarios,
            onDismiss = { showVeterinarioDialog = false }
        ) { vet ->
            viewModel.seleccionarVeterinario(vet)
            showVeterinarioDialog = false
        }
    }
}

// ==================== COMPONENTES ====================

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
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = if (isSelected) PrimaryBlue else TextSecondary
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                fontSize = 15.sp,
                color = if (isSelected) TextPrimary else TextSecondary,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
        Icon(
            if (isSelected) Icons.Default.CheckCircle else Icons.Default.ChevronRight,
            contentDescription = null,
            tint = if (isSelected) AccentGreen else TextTertiary
        )
    }
}