package com.example.vetapp_usuario.data.remote

import com.example.vetapp_usuario.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // AUTH
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // MASCOTAS
    @GET("api/mascotas")
    suspend fun getMascotas(@Header("Authorization") token: String): Response<List<Mascota>>

    @GET("api/mascotas/{id}")
    suspend fun getMascotaById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Mascota>

    @POST("api/mascotas")
    suspend fun crearMascota(
        @Header("Authorization") token: String,
        @Body request: MascotaRequest
    ): Response<Mascota>

    @PUT("api/mascotas/{id}")
    suspend fun actualizarMascota(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: MascotaRequest
    ): Response<Mascota>

    @DELETE("api/mascotas/{id}")
    suspend fun eliminarMascota(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    // SUCURSALES
    @GET("api/sucursales")
    suspend fun getSucursales(): Response<List<Sucursal>>

    @GET("api/sucursales/{id}")
    suspend fun getSucursalById(@Path("id") id: Int): Response<Sucursal>

    @GET("api/sucursales/{id}/veterinarios")
    suspend fun getVeterinariosBySucursal(@Path("id") sucursalId: Int): Response<List<Veterinario>>

    // CITAS - ✅ USANDO CitaUsuarioDTO
    @GET("api/citas")
    suspend fun getMisCitas(@Header("Authorization") token: String): Response<List<CitaUsuarioDTO>>

    @GET("api/citas/{id}")
    suspend fun getCitaById(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<CitaUsuarioDTO>  // ✅ Cambio aquí

    @POST("api/citas")
    suspend fun crearCita(
        @Header("Authorization") token: String,
        @Body request: CitaRequest
    ): Response<CitaUsuarioDTO>  // ✅ Cambio aquí

    @DELETE("api/citas/{id}")
    suspend fun cancelarCita(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    // RESEÑAS
    @POST("api/resenas")
    suspend fun crearResena(
        @Header("Authorization") token: String,
        @Body request: ResenaRequest
    ): Response<Resena>

    @GET("api/resenas/veterinario/{veterinarioId}")
    suspend fun getResenasByVeterinario(@Path("veterinarioId") veterinarioId: Int): Response<List<Resena>>
}