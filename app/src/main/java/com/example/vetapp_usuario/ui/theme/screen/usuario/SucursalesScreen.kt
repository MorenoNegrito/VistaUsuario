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
import com.example.vetapp_usuario.data.model.Sucursal
import com.example.vetapp_usuario.navigation.AppRoutes
import com.example.vetapp_usuario.ui.theme.*
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SucursalesScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSucursales()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sucursales",
                        fontWeight = FontWeight.Bold
                    )
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

                uiState.sucursales.isEmpty() -> {
                    EmptySucursalesState(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.sucursales) { sucursal ->
                            SucursalCard(
                                sucursal = sucursal,
                                onClick = {
                                    viewModel.seleccionarSucursal(sucursal)
                                    navController.navigate(AppRoutes.DetalleSucursal.create(sucursal.id))
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
fun SucursalCard(
    sucursal: Sucursal,
    onClick: () -> Unit
) {
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
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = IconBackground
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalHospital,
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
                        text = sucursal.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    if (sucursal.activo != false) {
                        Surface(
                            modifier = Modifier.padding(top = 4.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFD1FAE5)
                        ) {
                            Text(
                                text = "Activo",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 11.sp,
                                color = AccentGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TextTertiary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = BorderLight)

            Spacer(modifier = Modifier.height(12.dp))

            // Detalles
            SucursalInfoRow(
                icon = Icons.Default.LocationOn,
                text = sucursal.direccion
            )

            Spacer(modifier = Modifier.height(8.dp))

            SucursalInfoRow(
                icon = Icons.Default.Phone,
                text = sucursal.telefono
            )

            Spacer(modifier = Modifier.height(8.dp))

            SucursalInfoRow(
                icon = Icons.Default.Schedule,
                text = sucursal.horarioAtencion
            )

            if (!sucursal.ciudad.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                SucursalInfoRow(
                    icon = Icons.Default.LocationCity,
                    text = sucursal.ciudad
                )
            }
        }
    }
}

@Composable
fun SucursalInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = TextSecondary
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun EmptySucursalesState(
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
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.padding(24.dp),
                tint = PrimaryBlue
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No hay sucursales disponibles",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Por favor, intenta más tarde",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}