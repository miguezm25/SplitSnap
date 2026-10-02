package com.miguelzapata.splitsnap.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import com.miguelzapata.splitsnap.ui.viewmodel.GruposViewModel

@Composable
fun GruposScreen(
    onGrupoClick: (Long) -> Unit,
    viewModel: GruposViewModel = hiltViewModel()
) {
    val grupos by viewModel.grupos.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var miembrosTexto by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Text(text = "Crear grupo")

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

            Text(text = "Mis grupos")

            LazyColumn {
                items(grupos) { grupo ->
                    Text(
                        text = "${grupo.nombre} (${grupo.miembros.size} miembros)",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onGrupoClick(grupo.id) }
                    )
                }
            }
        }
    }
}