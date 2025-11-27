package com.example.vetapp_usuario.ui.theme.screen.shared

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
import androidx.navigation.NavController
import com.example.vetapp_usuario.data.local.UsuarioPreferences
import com.example.vetapp_usuario.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    prefs: UsuarioPreferences,
    onLogout: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val userName by prefs.userName.collectAsState(initial = "Usuario")
    val userEmail by prefs.userEmail.collectAsState(initial = "email@ejemplo.com")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Configuración",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Perfil del usuario
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BackgroundWhite
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(70.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = IconBackground
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.padding(18.dp),
                            tint = PrimaryBlue
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = userName ?: "Usuario",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = userEmail ?: "email@ejemplo.com",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                    }

                    IconButton(onClick = { /* Edit profile */ }) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = PrimaryBlue
                        )
                    }
                }
            }

            // Sección Cuenta
            Text(
                text = "Cuenta",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BackgroundWhite
                )
            ) {
                Column {
                    SettingsItem(
                        icon = Icons.Default.Person,
                        title = "Información Personal",
                        subtitle = "Edita tu perfil",
                        onClick = { /* Navigate to profile */ }
                    )

                    Divider(color = BorderLight, modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Lock,
                        title = "Cambiar Contraseña",
                        subtitle = "Actualiza tu contraseña",
                        onClick = { /* Navigate to change password */ }
                    )

                    Divider(color = BorderLight, modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Notifications,
                        title = "Notificaciones",
                        subtitle = "Gestiona tus alertas",
                        onClick = { /* Navigate to notifications */ }
                    )
                }
            }

            // Sección Preferencias
            Text(
                text = "Preferencias",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BackgroundWhite
                )
            ) {
                Column {
                    SettingsItem(
                        icon = Icons.Default.Settings,
                        title = "Idioma",
                        subtitle = "Español",
                        onClick = { /* Change language */ }
                    )

                    Divider(color = BorderLight, modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Star,
                        title = "Tema",
                        subtitle = "Claro",
                        onClick = { /* Change theme */ }
                    )
                }
            }

            // Sección Soporte
            Text(
                text = "Soporte",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = BackgroundWhite
                )
            ) {
                Column {
                    SettingsItem(
                        icon = Icons.Default.Search,
                        title = "Ayuda y Soporte",
                        subtitle = "Obtén asistencia",
                        onClick = { /* Navigate to help */ }
                    )

                    Divider(color = BorderLight, modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "Acerca de",
                        subtitle = "Versión 1.0.0",
                        onClick = { /* Show about */ }
                    )

                    Divider(color = BorderLight, modifier = Modifier.padding(horizontal = 16.dp))

                    SettingsItem(
                        icon = Icons.Default.MoreVert,
                        title = "Términos y Condiciones",
                        subtitle = "Lee nuestras políticas",
                        onClick = { /* Show terms */ }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón Cerrar Sesión
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentRed
                )
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Dialog de confirmación logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = AccentOrange,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "¿Cerrar sesión?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("¿Estás seguro de que deseas cerrar sesión?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            prefs.clearAll()
                            showLogoutDialog = false
                            onLogout()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(44.dp),
            shape = RoundedCornerShape(10.dp),
            color = IconBackground
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(10.dp),
                tint = PrimaryBlue
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = TextSecondary
            )
        }

        Icon(
            Icons.Default.PlayArrow,
            contentDescription = null,
            tint = TextTertiary
        )
    }
}