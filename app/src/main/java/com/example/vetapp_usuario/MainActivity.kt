package com.example.vetapp_usuario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.vetapp_usuario.data.local.UsuarioPreferences
import com.example.vetapp_usuario.data.repository.UsuarioRepository
import com.example.vetapp_usuario.navigation.AppNavigation
import com.example.vetapp_usuario.ui.theme.VetAppTheme
import com.example.vetapp_usuario.viewmodel.AuthViewModel
import com.example.vetapp_usuario.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar dependencias
        val preferences = UsuarioPreferences(this)
        val repository = UsuarioRepository()

        // Crear ViewModels usando constructor por defecto
        val usuarioViewModel: UsuarioViewModel =
            ViewModelProvider(this)[UsuarioViewModel::class.java]

        val authViewModel: AuthViewModel =
            ViewModelProvider(this)[AuthViewModel::class.java]

        setContent {
            // 🔥 CAMBIO: Usar VetAppTheme en lugar de MaterialTheme
            VetAppTheme {
                Surface {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        usuarioViewModel = usuarioViewModel,
                        preferences = preferences
                    )
                }
            }
        }
    }
}