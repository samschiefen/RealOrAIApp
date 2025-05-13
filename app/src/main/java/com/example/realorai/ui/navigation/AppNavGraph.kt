package com.example.realorai.ui.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.realorai.data.loadImagesFromAssets
import com.example.realorai.ui.screens.HomePage
import com.example.realorai.ui.screens.HomePageDestination
import com.example.realorai.ui.screens.QuestionScreen
import com.example.realorai.ui.screens.ResultScreen
import com.example.realorai.ui.screens.TestImageDestination
import com.example.realorai.ui.screens.TestImagePage
import com.example.realorai.ui.screens.TestSelfDestination
import com.example.realorai.ui.screens.TestYourselfPage

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun AppNavHost(
    navController: NavHostController,
    // homePageViewModel: HomePageViewModel,
    // testImageViewModel: TestImageViewModel,
    // testSelfViewModel: TestSelfViewModel,
    context: Context,
    modifier: Modifier = Modifier,
) {
    val imageList = remember { loadImagesFromAssets(context) }

    NavHost(
        navController = navController,
        startDestination = HomePageDestination.route,
        modifier = modifier
    ) {
        composable(route = HomePageDestination.route) {
            // val homePageUiState by homePageViewModel.homePageUiState.collectAsState()

            HomePage(
                // homePageUiState = homePageUiState,
                // homePageViewModel = homePageViewModel,
                testSelf = {
                    navController.navigate(TestSelfDestination.route)
                },
                testImage = {
                    navController.navigate(TestImageDestination.route)
                },
            )
        }
        composable(
            route = TestSelfDestination.route,
        ) {
            TestYourselfPage(
                // navigateBack = { navController.popBackStack() },
                takeTest = {
                    navController.navigate("question_screen")
                },
            )
        }

        // Question Screen (Navigates through test questions)
        composable(route = "question_screen") {
            QuestionScreen(imageList) { correct, total ->
                navController.navigate("result_screen/$correct/$total")
            }
        }

        // Result Screen (Shows the final score)
        composable(
            route = "result_screen/{correct}/{total}",
            arguments = listOf(
                navArgument("correct") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 1
            ResultScreen(
                goHome = {
                    navController.navigate(HomePageDestination.route)
                },
                correct,
                total
            )
        }

        composable(
            route = TestImageDestination.route,
        )
        {
            TestImagePage(
                // navigateBack = { navController.popBackStack() },
                // regularNoteViewModel = regularNoteViewModel,
                // mainScreenViewModel = mainScreenViewModel
            )
        }
    }
}