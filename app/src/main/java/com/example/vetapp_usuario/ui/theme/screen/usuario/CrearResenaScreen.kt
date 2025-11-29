package com.example.vetapp_usuario.ui.theme.screen.usuario

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vetapp_usuario.data.model.CitaUsuarioDTO
import com.example.vetapp_usuario.data.model.ResenaRequest
import com.example.vetapp_usuario.ui.theme.*
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearResenaScreen(
    token: String,
    veterinarioId: Int,
    viewModel: UsuarioViewModel,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    var estrellas by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }
    var citaSeleccionada by remember { mutableStateOf<CitaUsuarioDTO?>(null) }
    var showCitaSelector by remember { mutableStateOf(false) }

    // Cargar citas del usuario
    LaunchedEffect(token) {
        if (token.isNotBlank()) {
            viewModel.loadCitas(token)
        }
    }

    // Filtrar citas COMPLETADAS con este veterinario
    val citasDisponibles = remember(uiState.citas) {
        uiState.citas.filter { cita ->
            cita.estado.uppercase() == "COMPLETADA" &&
                    cita.veterinarioNombre.contains("Pedro", ignoreCase = true) // Temporal: filtrar por nombre
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calificar Atención", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onSuccess) {
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Encabezado
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = IconBackground
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = BackgroundWhite
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.padding(14.dp),
                            tint = PrimaryBlue
                        )
                    }

                    Column {
                        Text(
                            text = "Califica tu experiencia",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Tu opinión nos ayuda a mejorar",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Selector de Cita
            if (citasDisponibles.isNotEmpty()) {
                Text(
                    text = "Selecciona la cita a calificar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCitaSelector = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BackgroundWhite
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (citaSeleccionada != null) {
                                    formatFechaCrearResena(citaSeleccionada!!.fechaHora)
                                } else {
                                    "Seleccionar cita"
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                            if (citaSeleccionada != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = citaSeleccionada!!.motivo,
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = PrimaryBlue
                        )
                    }
                }
            } else {
                // Si no hay citas disponibles
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFEE2E2)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = AccentRed
                        )
                        Text(
                            text = "No tienes citas completadas con este veterinario. Debes tener al menos una cita finalizada para dejar una reseña.",
                            fontSize = 13.sp,
                            color = TextPrimary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Calificación con estrellas
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "¿Cómo calificarías la atención?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    // Estrellas
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        (1..5).forEach { star ->
                            Icon(
                                imageVector = if (star <= estrellas) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clickable { estrellas = star },
                                tint = if (star <= estrellas) AccentOrange else BorderMedium
                            )
                        }
                    }

                    // Texto de calificación
                    if (estrellas > 0) {
                        Text(
                            text = when (estrellas) {
                                1 -> "Muy insatisfecho"
                                2 -> "Insatisfecho"
                                3 -> "Neutral"
                                4 -> "Satisfecho"
                                5 -> "Muy satisfecho"
                                else -> ""
                            },
                            fontSize = 14.sp,
                            color = AccentOrange,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Comentario
            Text(
                text = "Cuéntanos tu experiencia",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundWhite)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = comentario,
                        onValueChange = { comentario = it },
                        placeholder = {
                            Text(
                                "Escribe aquí tus comentarios sobre la atención recibida...",
                                color = TextTertiary
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp),
                        minLines = 5,
                        maxLines = 8,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = BorderLight,
                            cursorColor = PrimaryBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${comentario.length}/500 caracteres",
                        fontSize = 12.sp,
                        color = TextTertiary,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón enviar
            Button(
                onClick = {
                    when {
                        citaSeleccionada == null -> {
                            Toast.makeText(
                                context,
                                "Por favor selecciona una cita",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        estrellas == 0 -> {
                            Toast.makeText(
                                context,
                                "Por favor selecciona una calificación",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        comentario.isBlank() -> {
                            Toast.makeText(
                                context,
                                "Por favor escribe un comentario",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                            scope.launch {
                                val request = ResenaRequest(
                                    citaId = citaSeleccionada!!.id.toInt(),
                                    estrellas = estrellas,
                                    comentario = comentario
                                )

                                viewModel.crearResena(token, request) {
                                    Toast.makeText(
                                        context,
                                        "✅ Reseña publicada exitosamente",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    onSuccess()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = !uiState.isLoading &&
                        citasDisponibles.isNotEmpty() &&
                        citaSeleccionada != null &&
                        estrellas > 0 &&
                        comentario.isNotBlank(),
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
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Enviar Reseña",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Dialog selector de citas
    if (showCitaSelector) {
        AlertDialog(
            onDismissRequest = { showCitaSelector = false },
            title = {
                Text(
                    text = "Selecciona la cita",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    citasDisponibles.forEach { cita ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    citaSeleccionada = cita
                                    showCitaSelector = false
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (citaSeleccionada?.id == cita.id)
                                    Color(0xFFDEEBFF) else IconBackground
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = formatFechaCrearResena(cita.fechaHora),
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
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Mascota: ${cita.mascotaNombre}",
                                    fontSize = 11.sp,
                                    color = TextTertiary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCitaSelector = false }) {
                    Text("Cancelar")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

fun formatFechaCrearResena(fechaHora: String): String {
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