package com.miguelzapata.splitsnap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.miguelzapata.splitsnap.ui.components.EstadoVacio
import com.miguelzapata.splitsnap.ui.components.TarjetaGrupo
import com.miguelzapata.splitsnap.ui.components.TarjetaSuave
import com.miguelzapata.splitsnap.ui.viewmodel.GruposViewModel

@Composable
fun GruposScreen(
    onGrupoClick: (Long) -> Unit,
    viewModel: GruposViewModel = hiltViewModel()
) {
    val grupos by viewModel.grupos.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var miembrosTexto by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "SplitSnap",
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = "Divide gastos sin complicaciones",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                TarjetaSuave {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Nuevo grupo",
                            style = MaterialTheme.typography.titleMedium
                        )
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre del grupo") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = miembrosTexto,
                            onValueChange = { miembrosTexto = it },
                            label = { Text("Miembros (separados por coma)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                val miembros = miembrosTexto
                                    .split(",")
                                    .map { it.trim() }
                                    .filter { it.isNotEmpty() }

                                if (nombre.isNotBlank() && miembros.isNotEmpty()) {
                                    viewModel.crearGrupo(nombre, miembros)
                                    nombre = ""
                                    miembrosTexto = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Crear grupo")
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Mis grupos",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (grupos.isEmpty()) {
                item {
                    EstadoVacio(
                        icono = Icons.Filled.Person,
                        titulo = "Aún no tienes grupos",
                        mensaje = "Crea el primero con el formulario de arriba para empezar a dividir gastos."
                    )
                }
            } else {
                items(grupos, key = { it.id }) { grupo ->
                    TarjetaGrupo(
                        grupo = grupo,
                        onClick = { onGrupoClick(grupo.id) }
                    )
                }
            }
        }
    }
}