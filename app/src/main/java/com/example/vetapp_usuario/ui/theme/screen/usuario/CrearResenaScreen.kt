package com.example.vetapp_usuario.ui.theme.screen.usuario

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vetapp_usuario.data.model.ResenaRequest
import com.example.vetapp_usuario.ui.theme.*
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearResenaScreen(
    token: String,
    citaId: Int,
    veterinarioId: Int,
    viewModel: UsuarioViewModel,
    onSuccess: () -> Unit
) {
    var estrellas by remember { mutableStateOf(0) }
    var comentario by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Calificar Atención",
                        fontWeight = FontWeight.Bold
                    )
                },
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

            // Información
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0FDF4)
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
                        tint = AccentGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Tu reseña será visible para otros usuarios y ayudará a mejorar nuestro servicio.",
                        fontSize = 13.sp,
                        color = TextPrimary,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón enviar
            Button(
                onClick = {
                    val request = ResenaRequest(
                        citaId = citaId,
                        estrellas = estrellas,
                        comentario = comentario
                    )
                    viewModel.crearResena(token, request, onSuccess)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = !uiState.isLoading && estrellas > 0 && comentario.isNotBlank(),
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
}