package com.example.ica2_prep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ica2_prep.data.AppViewModel
import com.example.ica2_prep.ui.MainScreen
import com.example.ica2_prep.ui.OnboardingScreen
import com.example.ica2_prep.ui.SplashScreen

/*
TODO:
- Add composable templates
- Email utils
- More internal services
- More transitions
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AppNavigation() }
    }

    @Composable
    fun AppNavigation() {
        val viewModel: AppViewModel = viewModel()
        val navController = rememberNavController()

        /*
        // To navigate to new activity
        navController.navigate("secondary")

        // To prevent returning
        navController.navigate("secondary") {
            popUpTo("main") { inclusive = true }
        }
         */

        NavHost(navController, startDestination = "main") { // Change to "splash" or "onboarding" as needed
            composable("splash") { SplashScreen(navController) }
            composable("onboarding") { OnboardingScreen(navController) }
            composable("main") { MainScreen(viewModel, navController) }

            /*
            // To pass data into new activity
            composable("chapter/{chapterNum}") { backStackEntry ->
                val chapterNum = backStackEntry.arguments?.getString("chapterNum")
                ChapterScreen(viewModel, chapterNum!!.toInt())
            }
            // And to navigate, navController.navigate("chapter/$index")
             */

            /*
            // For transitions
            composable(
                "secondary",
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                    )
                }
            ) {
                SecondaryScreen(viewModel, navController)
            }
             */
        }
    }
}
