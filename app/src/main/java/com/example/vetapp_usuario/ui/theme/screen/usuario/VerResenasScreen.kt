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
import com.example.vetapp_usuario.data.model.Resena
import com.example.vetapp_usuario.ui.theme.*
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerResenasScreen(
    veterinarioId: Int,
    viewModel: UsuarioViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(veterinarioId) {
        viewModel.loadResenasByVeterinario(veterinarioId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reseñas",
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

                uiState.resenas.isEmpty() -> {
                    EmptyResenasState(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Resumen de calificaciones
                        item {
                            ResumenCalificaciones(
                                resenas = uiState.resenas
                            )
                        }

                        item {
                            Text(
                                text = "Todas las Reseñas (${uiState.resenas.size})",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }

                        // Lista de reseñas
                        items(uiState.resenas) { resena ->
                            ResenaCard(resena = resena)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResumenCalificaciones(
    resenas: List<Resena>
) {
    val promedio = if (resenas.isNotEmpty()) {
        resenas.map { it.estrellas }.average()
    } else 0.0

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Calificación Promedio",
                fontSize = 14.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )

            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = String.format("%.1f", promedio),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = AccentOrange,
                    modifier = Modifier.size(36.dp)
                )
            }

            Text(
                text = "Basado en ${resenas.size} reseñas",
                fontSize = 13.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Distribución de estrellas
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                (5 downTo 1).forEach { stars ->
                    val count = resenas.count { it.estrellas == stars }
                    val percentage = if (resenas.isNotEmpty()) {
                        (count.toFloat() / resenas.size) * 100
                    } else 0f

                    DistribucionEstrellas(
                        estrellas = stars,
                        cantidad = count,
                        porcentaje = percentage
                    )
                }
            }
        }
    }
}

@Composable
fun DistribucionEstrellas(
    estrellas: Int,
    cantidad: Int,
    porcentaje: Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$estrellas",
            fontSize = 13.sp,
            color = TextSecondary,
            modifier = Modifier.width(12.dp)
        )

        Icon(
            Icons.Default.Star,
            contentDescription = null,
            tint = AccentOrange,
            modifier = Modifier.size(14.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
        ) {
            // Fondo
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(4.dp),
                color = BorderLight
            ) {}

            // Barra de progreso
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(porcentaje / 100f),
                shape = RoundedCornerShape(4.dp),
                color = AccentOrange
            ) {}
        }

        Text(
            text = "$cantidad",
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.width(24.dp)
        )
    }
}

@Composable
fun ResenaCard(
    resena: Resena
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header con usuario y calificación
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = IconBackground
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            tint = PrimaryBlue
                        )
                    }

                    Column {
                        Text(
                            text = resena.usuario?.nombre ?: "Usuario",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = formatFechaResena(resena.fechaCreacion),
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Estrellas
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(resena.estrellas) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = AccentOrange,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    repeat(5 - resena.estrellas) {
                        Icon(
                            Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = BorderMedium,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Comentario
            Text(
                text = resena.comentario,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun EmptyResenasState(
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
                imageVector = Icons.Default.StarBorder,
                contentDescription = null,
                modifier = Modifier.padding(24.dp),
                tint = PrimaryBlue
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Sin reseñas aún",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Este veterinario aún no tiene reseñas",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

fun formatFechaResena(fecha: String): String {
    // Simplificado - implementa formato real
    return fecha.take(10)
}