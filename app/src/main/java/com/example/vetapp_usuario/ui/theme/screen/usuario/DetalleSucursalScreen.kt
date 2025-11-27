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
import com.example.vetapp_usuario.data.model.Veterinario
import com.example.vetapp_usuario.ui.theme.*
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleSucursalScreen(
    sucursalId: Int,
    viewModel: UsuarioViewModel,
    navController: androidx.navigation.NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val sucursal = uiState.sucursalSeleccionada

    LaunchedEffect(sucursalId) {
        viewModel.loadSucursalDetail(sucursalId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = sucursal?.nombre ?: "Detalle Sucursal",
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
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = PrimaryBlue
                )
            } else if (sucursal != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Información de la sucursal
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = BackgroundWhite
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                InfoRow(
                                    icon = Icons.Default.LocationOn,
                                    label = "Dirección",
                                    value = sucursal.direccion
                                )

                                Divider(color = BorderLight)

                                InfoRow(
                                    icon = Icons.Default.Phone,
                                    label = "Teléfono",
                                    value = sucursal.telefono
                                )

                                Divider(color = BorderLight)

                                InfoRow(
                                    icon = Icons.Default.Schedule,
                                    label = "Horario",
                                    value = sucursal.horarioAtencion
                                )

                                if (!sucursal.ciudad.isNullOrBlank()) {
                                    Divider(color = BorderLight)

                                    InfoRow(
                                        icon = Icons.Default.LocationCity,
                                        label = "Ciudad",
                                        value = sucursal.ciudad
                                    )
                                }
                            }
                        }
                    }

                    // Servicios disponibles
                    if (!sucursal.serviciosDisponibles.isNullOrBlank()) {
                        item {
                            Text(
                                text = "Servicios Disponibles",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = BackgroundWhite
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Text(
                                        text = sucursal.serviciosDisponibles,
                                        fontSize = 14.sp,
                                        color = TextSecondary,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }

                    // Veterinarios
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Veterinarios",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = IconBackground
                            ) {
                                Text(
                                    text = "${uiState.veterinarios.size} disponibles",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 12.sp,
                                    color = PrimaryBlue,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    items(uiState.veterinarios) { veterinario ->
                        VeterinarioCard(
                            veterinario = veterinario,
                            onClick = {
                                viewModel.seleccionarVeterinario(veterinario)
                                navController.navigate(
                                    com.example.vetapp_usuario.navigation.AppRoutes.VerResenas.create(veterinario.id)
                                )
                            }
                        )
                    }
                }
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
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                color = TextPrimary
            )
        }
    }
}

@Composable
fun VeterinarioCard(
    veterinario: Veterinario,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = IconBackground
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = PrimaryBlue
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Dr. ${veterinario.nombre}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = veterinario.especialidad,
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                if ((veterinario.totalResenas ?: 0) > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = AccentOrange
                        )
                        Text(
                            text = "${veterinario.promResenas ?: 0.0}",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "(${veterinario.totalResenas})",
                            fontSize = 12.sp,
                            color = TextTertiary
                        )
                    }
                } else {
                    Text(
                        text = "Sin reseñas aún",
                        fontSize = 12.sp,
                        color = TextTertiary
                    )
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextTertiary
            )
        }
    }
}