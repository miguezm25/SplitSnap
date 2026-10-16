package com.miguelzapata.splitsnap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miguelzapata.splitsnap.ui.Rutas
import com.miguelzapata.splitsnap.ui.screens.BalanceScreen
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
                    ) { backStackEntry ->
                        val grupoId = backStackEntry.arguments?.getLong("grupoId") ?: 0L
                        DetalleScreen(
                            onVolverClick = { navController.popBackStack() },
                            onBalanceClick = { navController.navigate(Rutas.balanceConId(grupoId)) }
                        )
                    }
                    composable(
                        route = Rutas.BALANCE,
                        arguments = listOf(navArgument("grupoId") { type = NavType.LongType })
                    ) {
                        BalanceScreen(
                            onVolverClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}