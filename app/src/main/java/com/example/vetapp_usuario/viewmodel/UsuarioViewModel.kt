package com.example.vetapp_usuario.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetapp_usuario.data.model.*
import com.example.vetapp_usuario.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ✅ CAMBIO: citas usa CitaUsuarioDTO
data class UsuarioUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val mascotas: List<Mascota> = emptyList(),
    val citas: List<CitaUsuarioDTO> = emptyList(),
    val sucursales: List<Sucursal> = emptyList(),
    val veterinarios: List<Veterinario> = emptyList(),
    val resenas: List<Resena> = emptyList(),
    val mascotaSeleccionada: Mascota? = null,
    val veterinarioSeleccionado: Veterinario? = null,
    val sucursalSeleccionada: Sucursal? = null
)

class UsuarioViewModel(
    private val repo: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    private fun setLoading(loading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = loading)
    }

    private fun setError(msg: String?) {
        _uiState.value = _uiState.value.copy(error = msg)
    }

    // ==================== MASCOTAS ====================

    fun loadMascotas(token: String) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.getMascotas(token)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(mascotas = response.body() ?: emptyList())
                } else {
                    setError("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }

    fun crearMascota(token: String, request: MascotaRequest, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.crearMascota(token, request)
                if (response.isSuccessful) {
                    loadMascotas(token)
                    onSuccess()
                } else {
                    setError("Error al crear mascota: ${response.code()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }

    fun eliminarMascota(token: String, id: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.eliminarMascota(token, id)
                if (response.isSuccessful) {
                    loadMascotas(token)
                    onSuccess()
                } else {
                    setError("Error al eliminar mascota: ${response.code()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }

    // ==================== SUCURSALES ====================

    fun loadSucursales() {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.getSucursales()
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(sucursales = response.body() ?: emptyList())
                } else {
                    setError("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }

    fun loadSucursalDetail(id: Int) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.getSucursalById(id)
                if (response.isSuccessful) {
                    response.body()?.let { sucursal ->
                        _uiState.value = _uiState.value.copy(
                            sucursalSeleccionada = sucursal,
                            veterinarios = sucursal.veterinarios ?: emptyList()
                        )
                    }
                } else {
                    setError("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }

    fun loadVeterinariosBySucursal(sucursalId: Int) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.getVeterinariosBySucursal(sucursalId)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(veterinarios = response.body() ?: emptyList())
                } else {
                    setError("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }

    // ==================== CITAS ====================

    fun loadCitas(token: String) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.getMisCitas(token)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(citas = response.body() ?: emptyList())
                } else {
                    setError("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }


    fun getCitaDetail(token: String, citaId: Int, onResult: (CitaUsuarioDTO?) -> Unit) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {

                // 🔥🔥 AGREGAR LOGS AQUÍ 🔥🔥
                android.util.Log.d("DETALLE_CITA", "Llamando detalle de cita...")
                android.util.Log.d("DETALLE_CITA", "Token enviado: '$token'")
                android.util.Log.d("DETALLE_CITA", "ID enviado: $citaId")

                val resp = repo.getCitaById(token, citaId)

                android.util.Log.d("DETALLE_CITA", "Código respuesta: ${resp.code()}")
                android.util.Log.d("DETALLE_CITA", "Mensaje: ${resp.message()}")
                android.util.Log.d("DETALLE_CITA", "Body: ${resp.body()}")

                if (resp.isSuccessful) {
                    onResult(resp.body())
                } else {
                    setError("Error ${resp.code()}: ${resp.message()}")
                    onResult(null)
                }

            } catch (e: Exception) {
                android.util.Log.e("DETALLE_CITA", "Excepción: ${e.message}")
                setError(e.message ?: "Error de red")
                onResult(null)
            } finally {
                setLoading(false)
            }
        }
    }


    fun crearCita(token: String, request: CitaRequest, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.crearCita(token, request)
                if (response.isSuccessful) {
                    loadCitas(token)
                    onSuccess()
                } else {
                    setError("Error al crear cita: ${response.code()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }

    fun cancelarCita(token: String, id: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.cancelarCita(token, id)
                if (response.isSuccessful) {
                    loadCitas(token)
                    onSuccess()
                } else {
                    setError("Error al cancelar cita: ${response.code()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }

    // ==================== RESEÑAS ====================

    fun loadResenasByVeterinario(veterinarioId: Int) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.getResenasByVeterinario(veterinarioId)
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(resenas = response.body() ?: emptyList())
                } else {
                    setError("Error ${response.code()}: ${response.message()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }

    fun crearResena(token: String, request: ResenaRequest, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.crearResena(token, request)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    setError("Error al crear reseña: ${response.code()}")
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
            } finally {
                setLoading(false)
            }
        }
    }

    // ==================== SELECCIÓN ====================

    fun seleccionarMascota(mascota: Mascota?) {
        _uiState.value = _uiState.value.copy(mascotaSeleccionada = mascota)
    }

    fun seleccionarVeterinario(veterinario: Veterinario?) {
        _uiState.value = _uiState.value.copy(veterinarioSeleccionado = veterinario)
    }

    fun seleccionarSucursal(sucursal: Sucursal?) {
        _uiState.value = _uiState.value.copy(sucursalSeleccionada = sucursal)
    }

    // ==================== UTILS ====================

    fun clearError() {
        setError(null)
    }

    fun clearState() {
        _uiState.value = UsuarioUiState()
    }
}