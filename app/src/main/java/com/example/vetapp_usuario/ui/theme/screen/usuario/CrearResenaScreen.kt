package com.example.vetapp_usuario.ui.theme.screen.usuario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vetapp_usuario.data.local.UsuarioPreferences
import com.example.vetapp_usuario.data.model.ResenaRequest
import com.example.vetapp_usuario.navigation.AppRoutes
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ==================== CREAR RESEÑA SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun CrearResenaScreen(
    navController: NavController,
    viewModel: UsuarioViewModel,
    citaId: Int
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val preferences = remember { UsuarioPreferences(context) }
    val scope = rememberCoroutineScope()

    var estrellas by remember { mutableStateOf(5) }
    var comentario by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dejar Reseña") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "¿Cómo fue tu experiencia?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Rating con estrellas
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Calificación",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (1..5).forEach { star ->
                            IconButton(
                                onClick = { estrellas = star }
                            ) {
                                Icon(
                                    if (star <= estrellas) Icons.Default.Star else Icons.Default.Star,
                                    contentDescription = "$star estrellas",
                                    modifier = Modifier.size(48.dp),
                                    tint = if (star <= estrellas)
                                        MaterialTheme.colorScheme.tertiary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "$estrellas ${if (estrellas == 1) "estrella" else "estrellas"}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Comentario
            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Cuéntanos tu experiencia") },
                placeholder = { Text("Comparte tu opinión sobre la atención...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                minLines = 5,
                maxLines = 8
            )

            if (comentario.length < 10 && comentario.isNotEmpty()) {
                Text(
                    "La reseña debe tener al menos 10 caracteres",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    isSubmitting = true
                    scope.launch {
                        val token = preferences.token.first()
                        token?.let {
                            viewModel.crearResena(
                                token = it,
                                request = ResenaRequest(
                                    citaId = citaId,
                                    estrellas = estrellas,
                                    comentario = comentario
                                ),
                                onSuccess = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        isSubmitting = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = comentario.length >= 10 && !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Send, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Enviar Reseña")
                }
            }
        }
    }
}