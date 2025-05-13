package com.example.realorai.ui

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.realorai.ui.navigation.AppNavHost

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealOrAiApp(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current // Get the context

    AppNavHost(
        navController = navController,
        context = context // Pass the context to AppNavHost
    )
}