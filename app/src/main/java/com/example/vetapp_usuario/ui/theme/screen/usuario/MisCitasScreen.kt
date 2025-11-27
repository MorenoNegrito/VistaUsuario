package com.example.vetapp_usuario.ui.theme.screen.usuario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import com.example.vetapp_usuario.data.model.Cita
import com.example.vetapp_usuario.navigation.AppRoutes
import com.example.vetapp_usuario.ui.theme.*
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val token by remember { mutableStateOf("") } // Obtener de preferences

    LaunchedEffect(token) {
        if (token.isNotBlank()) {
            viewModel.loadCitas(token)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis Citas",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundWhite,
                    titleContentColor = TextPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AppRoutes.CrearCita.route) },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva cita")
            }
        },
        containerColor = BackgroundLight
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryBlue
                    )
                }

                uiState.citas.isEmpty() -> {
                    EmptyCitasState(
                        onCrearCita = { navController.navigate(AppRoutes.CrearCita.route) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.citas) { cita ->
                            CitaCard(
                                cita = cita,
                                onClick = {
                                    navController.navigate(AppRoutes.DetalleCita.create(cita.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CitaCard(
    cita: Cita,
    onClick: () -> Unit
) {
    val estadoColor = when (cita.estado.uppercase()) {
        "CONFIRMADA" -> StatusConfirmed
        "COMPLETADA" -> StatusCompleted
        "CANCELADA" -> StatusCancelled
        else -> StatusPending
    }

    val estadoBackground = when (cita.estado.uppercase()) {
        "CONFIRMADA" -> Color(0xFFDEEBFF)
        "COMPLETADA" -> Color(0xFFD1FAE5)
        "CANCELADA" -> Color(0xFFFEE2E2)
        else -> Color(0xFFFEF3C7)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = PrimaryBlue
                    )
                    Text(
                        text = formatFecha(cita.fechaHora),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = estadoBackground
                ) {
                    Text(
                        text = cita.estado,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        color = estadoColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = BorderLight)

            Spacer(modifier = Modifier.height(12.dp))

            // Información
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (cita.mascota != null) {
                    CitaInfoRow(
                        icon = Icons.Default.Pets,
                        label = "Mascota",
                        value = cita.mascota.nombre
                    )
                }

                if (cita.veterinario != null) {
                    CitaInfoRow(
                        icon = Icons.Default.Person,
                        label = "Veterinario",
                        value = "Dr. ${cita.veterinario.nombre}"
                    )
                }

                if (cita.sucursal != null) {
                    CitaInfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "Sucursal",
                        value = cita.sucursal.nombre
                    )
                }

                CitaInfoRow(
                    icon = Icons.Default.Notes,
                    label = "Motivo",
                    value = cita.motivoCita
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón ver detalles
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PrimaryBlue
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue)
            ) {
                Text("Ver Detalles")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun CitaInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = TextSecondary
        )
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = TextTertiary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun EmptyCitasState(
    onCrearCita: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(24.dp),
            color = IconBackground
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                modifier = Modifier.padding(24.dp),
                tint = PrimaryBlue
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No tienes citas",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Agenda tu primera cita con un veterinario",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onCrearCita,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Agendar Cita")
        }
    }
}

fun formatFecha(fechaHora: String): String {
    // Simplificado - implementa formato real según tu necesidad
    return fechaHora.take(10) // YYYY-MM-DD
}