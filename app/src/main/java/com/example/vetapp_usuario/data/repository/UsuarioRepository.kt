package com.example.vetapp_usuario.data.repository

import com.example.vetapp_usuario.data.model.*
import com.example.vetapp_usuario.data.remote.RetrofitClient
import retrofit2.Response

class UsuarioRepository {

    private val api = RetrofitClient.apiService

    // -------- AUTH --------
    suspend fun register(request: RegisterRequest): Response<LoginResponse> {
        return api.register(request)
    }

    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return api.login(request)
    }

    // -------- MASCOTAS --------
    suspend fun getMascotas(token: String) = api.getMascotas("Bearer $token")

    suspend fun getMascotaById(token: String, id: Int) =
        api.getMascotaById("Bearer $token", id)

    suspend fun crearMascota(token: String, request: MascotaRequest) =
        api.crearMascota("Bearer $token", request)

    suspend fun actualizarMascota(token: String, id: Int, request: MascotaRequest) =
        api.actualizarMascota("Bearer $token", id, request)

    suspend fun eliminarMascota(token: String, id: Int) =
        api.eliminarMascota("Bearer $token", id)

    // -------- SUCURSALES --------
    suspend fun getSucursales() = api.getSucursales()

    suspend fun getSucursalById(id: Int) = api.getSucursalById(id)

    suspend fun getVeterinariosBySucursal(id: Int) =
        api.getVeterinariosBySucursal(id)

    // -------- CITAS --------
    suspend fun getMisCitas(token: String) =
        api.getMisCitas("Bearer $token")

    suspend fun getCitaById(token: String, id: Int) =
        api.getCitaById("Bearer $token", id)

    suspend fun crearCita(token: String, request: CitaRequest) =
        api.crearCita("Bearer $token", request)

    suspend fun cancelarCita(token: String, id: Int) =
        api.cancelarCita("Bearer $token", id)

    // -------- RESEÑAS --------
    suspend fun crearResena(token: String, request: ResenaRequest) =
        api.crearResena("Bearer $token", request)

    suspend fun getResenasByVeterinario(id: Int) =
        api.getResenasByVeterinario(id)
}