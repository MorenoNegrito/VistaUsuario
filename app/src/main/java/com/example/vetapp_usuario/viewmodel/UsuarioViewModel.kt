package com.example.vetapp_usuario.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vetapp_usuario.data.model.*
import com.example.vetapp_usuario.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


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

    /**
     * Cargar listado de mascotas (usa token)
     */
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

    /**
     * Crear mascota
     */
    fun crearMascota(token: String, request: MascotaRequest, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.crearMascota(token, request)
                if (response.isSuccessful) {
                    // Recargar lista de mascotas
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

    /**
     * Eliminar mascota
     */
    fun eliminarMascota(token: String, id: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.eliminarMascota(token, id)
                if (response.isSuccessful) {
                    // Recargar lista de mascotas
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

    /**
     * Cargar sucursales
     */
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

    /**
     * Cargar detalle de sucursal con veterinarios
     */
    fun loadSucursalDetail(id: Int) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.getSucursalById(id)
                if (response.isSuccessful) {
                    response.body()?.let { sucursal ->
                        // Actualizar la sucursal seleccionada
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

    /**
     * Cargar veterinarios por sucursal
     */
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

    /**
     * Cargar citas del usuario (usa token)
     */
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

    /**
     * Obtener detalle de una cita
     */
    fun getCitaDetail(token: String, citaId: Int, onResult: (Cita?) -> Unit) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val resp = repo.getCitaById(token, citaId)
                if (resp.isSuccessful) {
                    onResult(resp.body())
                } else {
                    setError("Error ${resp.code()}: ${resp.message()}")
                    onResult(null)
                }
            } catch (e: Exception) {
                setError(e.message ?: "Error de red")
                onResult(null)
            } finally {
                setLoading(false)
            }
        }
    }

    /**
     * Crear cita
     */
    fun crearCita(token: String, request: CitaRequest, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.crearCita(token, request)
                if (response.isSuccessful) {
                    // Recargar lista de citas
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

    /**
     * Cancelar cita
     */
    fun cancelarCita(token: String, id: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            setLoading(true)
            setError(null)
            try {
                val response = repo.cancelarCita(token, id)
                if (response.isSuccessful) {
                    // Recargar lista de citas
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

    /**
     * Cargar reseñas por veterinario
     */
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

    /**
     * Crear reseña
     */
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