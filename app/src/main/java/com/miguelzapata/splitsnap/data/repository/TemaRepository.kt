package com.miguelzapata.splitsnap.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

@Singleton
class TemaRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val claveModoOscuro = booleanPreferencesKey("modo_oscuro")

    // null = el usuario nunca eligió, así que se sigue el modo del sistema
    val modoOscuro: Flow<Boolean?> = context.dataStore.data.map { it[claveModoOscuro] }

    suspend fun guardarModoOscuro(activo: Boolean) {
        context.dataStore.edit { it[claveModoOscuro] = activo }
    }
}