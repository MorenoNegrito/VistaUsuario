package com.example.vetapp_usuario.ui.theme.screen.usuario
import androidx.compose.foundation.clickable
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
import com.example.vetapp_usuario.data.model.MascotaRequest
import com.example.vetapp_usuario.navigation.AppRoutes
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearMascotaScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val preferences = remember { UsuarioPreferences(context) }
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("Perro") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var vacunas by remember { mutableStateOf("") }
    var alergias by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Mascota") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            // Dropdown Especie
            var especieExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = especieExpanded,
                onExpandedChange = { especieExpanded = !especieExpanded }
            ) {
                OutlinedTextField(
                    value = especie,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Especie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = especieExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = especieExpanded,
                    onDismissRequest = { especieExpanded = false }
                ) {
                    listOf("Perro", "Gato", "Ave", "Conejo", "Otro").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                especie = option
                                especieExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = raza,
                onValueChange = { raza = it },
                label = { Text("Raza") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = edad,
                onValueChange = { edad = it },
                label = { Text("Edad (ej: 3 años)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = peso,
                onValueChange = { peso = it },
                label = { Text("Peso (kg)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = color,
                onValueChange = { color = it },
                label = { Text("Color") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = vacunas,
                onValueChange = { vacunas = it },
                label = { Text("Vacunas (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = alergias,
                onValueChange = { alergias = it },
                label = { Text("Alergias (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        val token = preferences.token.first()
                        token?.let {
                            viewModel.crearMascota(
                                token = it,
                                request = MascotaRequest(
                                    nombre = nombre,
                                    especie = especie,
                                    raza = raza,
                                    edad = edad,
                                    peso = peso.toDoubleOrNull() ?: 0.0,
                                    color = color,
                                    vacunas = vacunas.ifEmpty { null },
                                    alergias = alergias.ifEmpty { null }
                                ),
                                onSuccess = { navController.popBackStack() }
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nombre.isNotBlank() && raza.isNotBlank() && edad.isNotBlank() && peso.isNotBlank()
            ) {
                Text("Registrar Mascota")
            }
        }
    }
}
