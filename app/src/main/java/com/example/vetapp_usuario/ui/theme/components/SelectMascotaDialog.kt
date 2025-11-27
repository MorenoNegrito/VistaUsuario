package com.example.vetapp_usuario.ui.theme.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vetapp_usuario.data.model.Mascota
import com.example.vetapp_usuario.data.model.Sucursal
import com.example.vetapp_usuario.data.model.Veterinario
import com.example.vetapp_usuario.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMascotaDialog(
    mascotas: List<Mascota>,
    onDismiss: () -> Unit,
    onSelect: (Mascota) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BackgroundWhite
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Selecciona una Mascota",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (mascotas.isEmpty()) {
                    Text(
                        text = "No tienes mascotas registradas",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(mascotas) { mascota ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelect(mascota) },
                                colors = CardDefaults.cardColors(containerColor = BackgroundLight)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(Icons.Default.Pets, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = mascota.nombre,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                        Text(
                                            text = "${mascota.especie} • ${mascota.raza}",
                                            fontSize = 13.sp,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                    Text("Cancelar")
                }
            }
        }
    }
}