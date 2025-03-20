package com.example.ica2_prep.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.ica2_prep.data.AppViewModel
import com.example.ica2_prep.ui.theme.ICA2_PrepTheme
import com.example.ica2_prep.utils.RequestNotificationPermission

@Composable
fun MainScreen(viewModel: AppViewModel, navController: NavController) {
    val context = LocalContext.current

    RequestNotificationPermission { } // Insert code to be run after permission is granted

    // For controllable dark theme
    // MaterialTheme(colorScheme = if (viewModel.isDarkTheme) darkColorScheme() else lightColorScheme()) {
    ICA2_PrepTheme {
        Scaffold(
            topBar = {},

            floatingActionButton = {},

            content = { innerPadding -> Column(Modifier.fillMaxSize().padding(innerPadding)) {} }
        )
    }
}