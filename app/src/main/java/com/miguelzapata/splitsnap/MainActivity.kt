package com.miguelzapata.splitsnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.miguelzapata.splitsnap.ui.Rutas
import com.miguelzapata.splitsnap.ui.screens.DetalleScreen
import com.miguelzapata.splitsnap.ui.screens.GruposScreen
import com.miguelzapata.splitsnap.ui.theme.SplitSnapTheme
import com.miguelzapata.splitsnap.ui.viewmodel.TestViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplitSnapTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Rutas.GRUPOS
                ) {
                    composable(Rutas.GRUPOS) {
                        GruposScreen(
                            onGrupoClick = {
                                navController.navigate(Rutas.DETALLE)
                            }
                        )
                    }
                    composable(Rutas.DETALLE) {
                        DetalleScreen(
                            onVolverClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
                }
            }

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SplitSnapTheme {
        Greeting("Android")
    }
}