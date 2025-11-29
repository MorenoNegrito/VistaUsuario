package com.example.vetapp_usuario.ui.theme.screen.usuario

import android.util.Log
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
import com.example.vetapp_usuario.data.model.CitaUsuarioDTO
import com.example.vetapp_usuario.data.model.Resena
import com.example.vetapp_usuario.ui.theme.*
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerResenasScreen(
    token: String,
    veterinarioId: Int,
    viewModel: UsuarioViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCitaDialog by remember { mutableStateOf(false) }

    // Cargar reseñas del veterinario
    LaunchedEffect(veterinarioId) {
        viewModel.loadResenasByVeterinario(veterinarioId)
    }

    // Cargar citas del usuario si tiene token
    LaunchedEffect(token, veterinarioId) {
        if (token.isNotBlank()) {
            Log.d("VerResenas", "Cargando citas para verificar si puede dejar reseña")
            viewModel.loadCitas(token)
        }
    }

    // Filtrar citas COMPLETADAS con este veterinario que NO tienen reseña
    val citasCompletadasSinResena = remember(uiState.citas, uiState.resenas) {
        uiState.citas.filter { cita ->
            cita.veterinarioId?.toInt() == veterinarioId &&
                    cita.estado.uppercase() == "COMPLETADA" &&
                    // Verificar que no tenga reseña ya creada
                    uiState.resenas.none { resena ->
                        resena.cita?.id == cita.id.toInt()
                    }
        }
    }

    // ✅ LOGS DE DEBUG
    LaunchedEffect(token, uiState.citas, citasCompletadasSinResena) {
        Log.d("VerResenas_DEBUG", "═══════════════════════════════")
        Log.d("VerResenas_DEBUG", "Token length: ${token.length}")
        Log.d("VerResenas_DEBUG", "Token isNotBlank: ${token.isNotBlank()}")
        Log.d("VerResenas_DEBUG", "VeterinarioId buscado: $veterinarioId")
        Log.d("VerResenas_DEBUG", "Total citas del usuario: ${uiState.citas.size}")
        Log.d("VerResenas_DEBUG", "Total reseñas: ${uiState.resenas.size}")

        uiState.citas.forEachIndexed { index, cita ->
            Log.d("VerResenas_DEBUG", "Cita $index: ID=${cita.id}, Estado=${cita.estado}, VetId=${cita.veterinarioId}, VetNombre=${cita.veterinarioNombre}")
        }

        Log.d("VerResenas_DEBUG", "Citas COMPLETADAS sin reseña: ${citasCompletadasSinResena.size}")
        citasCompletadasSinResena.forEach { cita ->
            Log.d("VerResenas_DEBUG", "  → Cita ID=${cita.id}, Motivo=${cita.motivo}")
        }

        Log.d("VerResenas_DEBUG", "Mostrar FAB: ${token.isNotBlank() && citasCompletadasSinResena.isNotEmpty()}")
        Log.d("VerResenas_DEBUG", "═══════════════════════════════")
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
        floatingActionButton = {
            // Mostrar FAB solo si el usuario tiene citas completadas sin reseña
            if (token.isNotBlank() && citasCompletadasSinResena.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        Log.d("VerResenas_FAB", "FAB clickeado!")
                        Log.d("VerResenas_FAB", "Citas sin reseña: ${citasCompletadasSinResena.size}")

                        if (citasCompletadasSinResena.size == 1) {
                            // Si solo tiene 1 cita, ir directo a crear reseña
                            val cita = citasCompletadasSinResena.first()
                            val ruta = "crear_resena/${cita.id.toInt()}/${veterinarioId}"
                            Log.d("VerResenas_FAB", "Navegando a: $ruta")
                            navController.navigate(ruta)
                        } else {
                            // Si tiene múltiples citas, mostrar selector
                            Log.d("VerResenas_FAB", "Mostrando dialog selector")
                            showCitaDialog = true
                        }
                    },
                    containerColor = AccentGreen,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Dejar Reseña")
                }
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

                        // Espaciado para el FAB
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }

    // Dialog para seleccionar cita a reseñar
    if (showCitaDialog) {
        AlertDialog(
            onDismissRequest = { showCitaDialog = false },
            title = {
                Text(
                    text = "Selecciona la cita a calificar",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    citasCompletadasSinResena.forEach { cita ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val ruta = "crear_resena/${cita.id.toInt()}/${veterinarioId}"
                                    Log.d("VerResenas_Dialog", "Cita seleccionada: ID=${cita.id}")
                                    Log.d("VerResenas_Dialog", "Navegando a: $ruta")
                                    showCitaDialog = false
                                    navController.navigate(ruta)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = IconBackground
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = formatFechaResenaScreen(cita.fechaHora),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = cita.motivo,
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCitaDialog = false }) {
                    Text("Cancelar")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
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

fun formatFechaResena(fecha: String?): String {
    if (fecha == null) return "Sin fecha"
    return try {
        fecha.take(10)
    } catch (e: Exception) {
        "Sin fecha"
    }
}

fun formatFechaResenaScreen(fechaHora: String): String {
    return try {
        val partes = fechaHora.split("T")
        if (partes.size >= 2) {
            val fecha = partes[0]
            val hora = partes[1].take(5)
            "$fecha a las $hora"
        } else {
            fechaHora
        }
    } catch (e: Exception) {
        fechaHora
    }
}