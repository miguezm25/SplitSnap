package com.miguelzapata.splitsnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miguelzapata.splitsnap.ui.Rutas
import com.miguelzapata.splitsnap.ui.screens.DetalleScreen
import com.miguelzapata.splitsnap.ui.screens.GruposScreen
import com.miguelzapata.splitsnap.ui.theme.SplitSnapTheme
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
                            onGrupoClick = { grupoId ->
                                navController.navigate(Rutas.detalleConId(grupoId))
                            }
                        )
                    }
                    composable(
                        route = Rutas.DETALLE,
                        arguments = listOf(navArgument("grupoId") { type = NavType.LongType })
                    ) {
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