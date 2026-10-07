package com.example.animefinder.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.animefinder.network.RetrofitClient
import com.example.animefinder.repository.AnimeRepository
import com.example.animefinder.ui.screen.DetailScreen
import com.example.animefinder.ui.screen.HomeScreen
import com.example.animefinder.viewmodel.AnimeViewModel
import com.example.animefinder.viewmodel.AnimeViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    // Setup Repository and ViewModel Factory
    val repository = AnimeRepository(RetrofitClient.apiService)
    val factory = AnimeViewModelFactory(repository)
    val viewModel: AnimeViewModel = viewModel(factory = factory)

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToDetail = { animeId ->
                    navController.navigate("detail/$animeId")
                }
            )
        }
        composable(
            route = "detail/{animeId}",
            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: return@composable
            DetailScreen(
                animeId = animeId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
