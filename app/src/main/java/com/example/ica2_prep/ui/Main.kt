package com.example.ica2_prep.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.ica2_prep.data.AppViewModel

@Composable
fun MainScreen(viewModel: AppViewModel, navController: NavController) {
    // To get context
    // val context = LocalContext.current

    // For controllable dark theme
    // MaterialTheme(colorScheme = if (viewModel.isDarkTheme) darkColorScheme() else lightColorScheme()) {}

    ToolbarScaffold("ICA 2") {
        // Content
    }
}
