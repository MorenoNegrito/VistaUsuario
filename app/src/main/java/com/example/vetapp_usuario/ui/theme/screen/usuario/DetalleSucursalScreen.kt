package com.example.vetapp_usuario.ui.theme.screen.usuario

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
import com.example.vetapp_usuario.data.model.Sucursal
import com.example.vetapp_usuario.data.model.Veterinario
import com.example.vetapp_usuario.ui.theme.*
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleSucursalScreen(
    sucursalId: Int,
    viewModel: UsuarioViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(sucursalId) {
        viewModel.loadSucursalDetail(sucursalId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.sucursalSeleccionada?.nombre ?: "Sucursal",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryBlue
                    )
                }

                uiState.sucursalSeleccionada == null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = AccentRed
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Sucursal no encontrada",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Información de la sucursal
                        item {
                            SucursalInfoCard(
                                sucursal = uiState.sucursalSeleccionada!!
                            )
                        }

                        // Título de veterinarios
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Veterinarios Disponibles",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${uiState.veterinarios.size}",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Lista de veterinarios
                        if (uiState.veterinarios.isEmpty()) {
                            item {
                                EmptyVeterinariosState()
                            }
                        } else {
                            items(uiState.veterinarios) { veterinario ->
                                VeterinarioCard(
                                    veterinario = veterinario,
                                    onVerResenas = {
                                        navController.navigate("ver_resenas/${veterinario.id}")
                                    },
                                    onDejarResena = {
                                        navController.navigate("crear_resena/${veterinario.id}")
                                    }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SucursalInfoCard(
    sucursal: Sucursal
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = IconBackground
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp),
                        tint = PrimaryBlue
                    )
                }

                Column {
                    Text(
                        text = sucursal.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = sucursal.ciudad ?: "",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }

            Divider(color = BorderLight)

            InfoRow(
                icon = Icons.Default.Place,
                label = "Dirección",
                value = sucursal.direccion
            )

            InfoRow(
                icon = Icons.Default.Phone,
                label = "Teléfono",
                value = sucursal.telefono
            )

            InfoRow(
                icon = Icons.Default.Schedule,
                label = "Horario",
                value = sucursal.horarioAtencion ?: "Consultar"
            )

            sucursal.serviciosDisponibles?.let { servicios ->
                InfoRow(
                    icon = Icons.Default.MedicalServices,
                    label = "Servicios",
                    value = servicios
                )
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
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
                fontSize = 14.sp,
                color = TextPrimary
            )
        }
    }
}

@Composable
fun VeterinarioCard(
    veterinario: Veterinario,
    onVerResenas: () -> Unit,
    onDejarResena: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = IconBackground
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp),
                        tint = PrimaryBlue
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Dr. ${veterinario.nombre}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    veterinario.especialidad?.let { especialidad ->
                        Text(
                            text = especialidad,
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            veterinario.licencia?.let { licencia ->
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF0FDF4)
                ) {
                    Text(
                        text = "Lic. $licencia",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        color = AccentGreen,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Divider(color = BorderLight)

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón Ver Reseñas
                OutlinedButton(
                    onClick = onVerResenas,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PrimaryBlue
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ver Reseñas", fontSize = 13.sp)
                }

                // Botón Dejar Reseña
                Button(
                    onClick = onDejarResena,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentGreen
                    )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Dejar Reseña", fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun EmptyVeterinariosState() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(20.dp),
                color = IconBackground
            ) {
                Icon(
                    imageVector = Icons.Default.PersonOff,
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp),
                    tint = PrimaryBlue
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Sin veterinarios disponibles",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Esta sucursal aún no tiene veterinarios asignados",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}