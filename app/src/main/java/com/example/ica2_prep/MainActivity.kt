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
        navController.navigate("main")

        // With data
        navController.navigate("chapter/$index")
         */

        NavHost(navController, startDestination = "splash") { // Change to "onboarding" or "main" as needed
            composable("splash") { SplashScreen(navController) }
            composable("onboarding") { OnboardingScreen(navController) }
            composable("main") { MainScreen(viewModel, navController) }

            /* Eg for passing data into new activity
            composable("chapter/{chapterNum}") { backStackEntry ->
                val chapterNum = backStackEntry.arguments?.getString("chapterNum")
                ChapterScreen(viewModel, chapterNum!!.toInt())
            }
             */
        }
    }
}
