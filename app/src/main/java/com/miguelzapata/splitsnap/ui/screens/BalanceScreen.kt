package com.miguelzapata.splitsnap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.miguelzapata.splitsnap.domain.Deuda
import com.miguelzapata.splitsnap.ui.components.EstadoVacio
import com.miguelzapata.splitsnap.ui.components.TarjetaSuave
import com.miguelzapata.splitsnap.ui.formatearMonto
import com.miguelzapata.splitsnap.ui.viewmodel.BalanceViewModel
import kotlin.math.abs

@Composable
fun BalanceScreen(
    onVolverClick: () -> Unit,
    viewModel: BalanceViewModel = hiltViewModel()
) {
    val grupo by viewModel.grupo.collectAsState()
    val estado by viewModel.uiState.collectAsState()

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
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver al grupo"
                        )
                    }
                    Text(
                        text = "Balance",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = grupo?.nombre ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Text(
                    text = "Quién debe a quién",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (estado.deudas.isEmpty()) {
                item {
                    EstadoVacio(
                        icono = Icons.Filled.CheckCircle,
                        titulo = "Todos están a mano",
                        mensaje = "No hay deudas pendientes en este grupo."
                    )
                }
            } else {
                items(estado.deudas) { deuda ->
                    TarjetaDeuda(deuda)
                }
            }

            item {
                Text(
                    text = "Balance por persona",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                TarjetaSuave {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        estado.balances.forEach { (nombre, balance) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = nombre,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                InsigniaBalance(balance)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaDeuda(deuda: Deuda) {
    TarjetaSuave {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = deuda.deudor,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "le debe a ${deuda.acreedor}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = formatearMonto(deuda.monto),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun InsigniaBalance(balance: Double) {
    val (fondo, texto, signo) = when {
        balance > 0.005 -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "+"
        )
        balance < -0.005 -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "-"
        )
        else -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            ""
        )
    }

    Box(
        modifier = Modifier
            .background(fondo, CircleShape)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$signo${formatearMonto(abs(balance))}",
            style = MaterialTheme.typography.labelLarge,
            color = texto
        )
    }
}