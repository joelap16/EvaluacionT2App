package com.cibertec.evaluaciont2app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cibertec.evaluaciont2app.screens.PersonaListScreen
import com.cibertec.evaluaciont2app.screens.PersonaFormScreen
import com.cibertec.evaluaciont2app.ui.theme.EvaluacionT2AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PersonaAppNavigation()
                }
            }
        }
    }
}

object Routes {
    const val PERSON_LIST = "person_list"
    const val PERSON_FORM = "person_form"
    const val PERSON_ID_ARG = "personId"
}

@Composable
fun PersonaAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.PERSON_LIST) {
        composable(Routes.PERSON_LIST) {
            PersonaListScreen(
                onNavigateToForm = { personaId ->
                    if (personaId == null) {
                        navController.navigate(Routes.PERSON_FORM)
                    } else {
                        navController.navigate("${Routes.PERSON_FORM}?${Routes.PERSON_ID_ARG}=${personaId}")
                    }
                }
            )
        }

        composable(
            route = "${Routes.PERSON_FORM}?${Routes.PERSON_ID_ARG}={${Routes.PERSON_ID_ARG}}",
            arguments = listOf(
                navArgument(Routes.PERSON_ID_ARG) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val personaId = backStackEntry.arguments?.getInt(Routes.PERSON_ID_ARG)
            PersonaFormScreen(
                personaId = if (personaId == -1) null else personaId,
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}