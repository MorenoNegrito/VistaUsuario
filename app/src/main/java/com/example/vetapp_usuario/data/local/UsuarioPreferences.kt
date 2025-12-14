package com.example.vetapp_usuario.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//CREAMOS UNA EXTENSION CONTEXT que agregaa la propediad dataStora, usamos la delegacion
//Propiedades y sirve de unica instancia (SINGLETON)
private val Context.dataStore by preferencesDataStore(name = "usuario_preferences")

//Por que tenemos context extendido? por que nos sirve para teteo y agregar dependencias
//futuro
class UsuarioPreferences(private val context: Context) {

    companion object {
        //Variables de preferencias (Preferences key)
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    }

    // Guardar token
    //Suspend fun (Ejecucion en segundo plano)
    //datatore edit escribe en el disco (IO)
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // Obtener token
    //Flow nos lanzara todos los datos con contestordata nos da todas las prefernces
    //map transformacion de datosy con preferences sacamos solo el token
    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    // Guardar user ID
    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    // Obtener user ID
    val userId: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID_KEY]
    }

    // Guardar nombre
    suspend fun saveUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
        }
    }

    // Obtener nombre
    val userName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY]
    }

    // Guardar email
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL_KEY] = email
        }
    }

    // Obtener email
    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL_KEY]
    }

    // Guardar estado login
    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
        }
    }

    // Obtener estado login
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }

    // Limpiar todo
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}