package com.example.vetapp_usuario.ui.theme.screen.usuario

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vetapp_usuario.data.model.Cita
import com.example.vetapp_usuario.ui.theme.*
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCitaScreen(
    token: String,
    citaId: Int,
    viewModel: UsuarioViewModel,
    onCancelSuccess: () -> Unit
) {
    var cita by remember { mutableStateOf<Cita?>(null) }
    var showCancelDialog by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(citaId) {
        viewModel.getCitaDetail(token, citaId) { citaResult ->
            cita = citaResult
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle de Cita",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancelSuccess) {
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryBlue
                )
            } else if (cita != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Estado de la cita
                    EstadoCitaCard(estado = cita!!.estado)

                    // Información principal
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            DetailRow(
                                icon = Icons.Default.CalendarToday,
                                label = "Fecha y Hora",
                                value = cita!!.fechaHora
                            )

                            Divider(color = BorderLight)

                            if (cita!!.mascota != null) {
                                DetailRow(
                                    icon = Icons.Default.Pets,
                                    label = "Mascota",
                                    value = cita!!.mascota!!.nombre
                                )
                                Divider(color = BorderLight)
                            }

                            if (cita!!.veterinario != null) {
                                DetailRow(
                                    icon = Icons.Default.Person,
                                    label = "Veterinario",
                                    value = "Dr. ${cita!!.veterinario!!.nombre}",
                                    subtitle = cita!!.veterinario!!.especialidad
                                )
                                Divider(color = BorderLight)
                            }

                            if (cita!!.sucursal != null) {
                                DetailRow(
                                    icon = Icons.Default.LocationOn,
                                    label = "Sucursal",
                                    value = cita!!.sucursal!!.nombre,
                                    subtitle = cita!!.sucursal!!.direccion
                                )
                            }
                        }
                    }

                    // Motivo
                    Text(
                        text = "Motivo de la Cita",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
                    ) {
                        Text(
                            text = cita!!.motivoCita,
                            modifier = Modifier.padding(20.dp),
                            fontSize = 14.sp,
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )
                    }

                    // Mensaje del cliente si existe
                    if (!cita!!.mensajeCliente.isNullOrBlank()) {
                        Text(
                            text = "Mensaje Adicional",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
                        ) {
                            Text(
                                text = cita!!.mensajeCliente!!,
                                modifier = Modifier.padding(20.dp),
                                fontSize = 14.sp,
                                color = TextSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    // Diagnóstico y tratamiento (si está completada)
                    if (cita!!.estado.uppercase() == "COMPLETADA") {

                        if (!cita!!.diagnostico.isNullOrBlank()) {
                            Text(
                                text = "Diagnóstico",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
                            ) {
                                Text(
                                    text = cita!!.diagnostico!!,
                                    modifier = Modifier.padding(20.dp),
                                    fontSize = 14.sp,
                                    color = TextSecondary,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        if (!cita!!.tratamiento.isNullOrBlank()) {
                            Text(
                                text = "Tratamiento",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
                            ) {
                                Text(
                                    text = cita!!.tratamiento!!,
                                    modifier = Modifier.padding(20.dp),
                                    fontSize = 14.sp,
                                    color = TextSecondary,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón cancelar (solo si está pendiente o confirmada)
                    if (cita!!.estado.uppercase() in listOf("PENDIENTE", "CONFIRMADA")) {
                        OutlinedButton(
                            onClick = { showCancelDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = AccentRed
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AccentRed)
                        ) {
                            Icon(Icons.Default.Cancel, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cancelar Cita", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

    // Dialog de confirmación
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = AccentOrange,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "¿Cancelar cita?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("Esta acción no se puede deshacer. ¿Estás seguro de cancelar esta cita?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.cancelarCita(token, cita!!.id) {
                            showCancelDialog = false
                            onCancelSuccess()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed
                    )
                ) {
                    Text("Cancelar Cita")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Volver", color = TextSecondary)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun EstadoCitaCard(estado: String) {
    val (color, bgColor, icon, mensaje) = when (estado.uppercase()) {
        "CONFIRMADA" -> listOf(
            StatusConfirmed,
            Color(0xFFDEEBFF),
            Icons.Default.CheckCircle,
            "Tu cita ha sido confirmada"
        )
        "COMPLETADA" -> listOf(
            StatusCompleted,
            Color(0xFFD1FAE5),
            Icons.Default.CheckCircle,
            "Cita completada exitosamente"
        )
        "CANCELADA" -> listOf(
            StatusCancelled,
            Color(0xFFFEE2E2),
            Icons.Default.Cancel,
            "Esta cita fue cancelada"
        )
        else -> listOf(
            StatusPending,
            Color(0xFFFEF3C7),
            Icons.Default.Schedule,
            "Esperando confirmación"
        )
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor as Color)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon as androidx.compose.ui.graphics.vector.ImageVector,
                contentDescription = null,
                tint = color as Color,
                modifier = Modifier.size(32.dp)
            )

            Column {
                Text(
                    text = estado.uppercase(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = mensaje as String,
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    subtitle: String? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = PrimaryBlue
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }
    }
}