package com.lelestacia.lelenimecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lelestacia.collection.CollectionScreen
import com.lelestacia.common.route.Screen
import com.lelestacia.detail.screen.DetailScreen
import com.lelestacia.explore.screen.explore.ExplorationScreen
import com.lelestacia.explore.screen.explore.ExplorationScreenViewModel
import com.lelestacia.lelenimecompose.ui.component.LeleNimeBottomBar
import com.lelestacia.lelenimecompose.ui.theme.LelenimeComposeTheme
import com.lelestacia.more.MoreScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LelenimeComposeTheme {
                val scope: CoroutineScope = rememberCoroutineScope()
                val navHostController: NavHostController = rememberNavController()
                val snackBarHostState: SnackbarHostState = remember {
                    SnackbarHostState()
                }
                Scaffold(
                    bottomBar = {
                        LeleNimeBottomBar(navController = navHostController)
                    },
                    snackbarHost = {
                        SnackbarHost(snackBarHostState)
                    }
                ) { paddingValue ->
                    NavHost(
                        navController = navHostController,
                        startDestination = Screen.Explore.route
                    ) {

                        composable(route = Screen.Explore.route) {
                            val viewModel: ExplorationScreenViewModel = hiltViewModel()
                            val uiState = viewModel.explorationScreenState.collectAsState()
                            ExplorationScreen(
                                screenState = uiState.value,
                                onEvent = viewModel::onEvent,
                                onAnimeClicked = { anime ->
                                    scope.launch {
                                        viewModel.insertOrUpdateAnimeHistory(anime = anime).join()
                                        navHostController.navigate(
                                            route = Screen.DetailAnimeScreen.createRoute(
                                                anime.malID
                                            )
                                        ) {
                                            restoreState = true
                                        }
                                    }
                                },
                                modifier = Modifier.padding(paddingValue)
                            )
                        }

                        composable(route = Screen.Collection.route) {
                            CollectionScreen(
                                modifier = Modifier.padding(paddingValue)
                            )
                        }

                        composable(route = Screen.More.route) {
                            MoreScreen(
                                modifier = Modifier.padding(paddingValue)
                            )
                        }

                        composable(
                            route = Screen.DetailAnimeScreen.route,
                            arguments = listOf(
                                navArgument(name = "mal_id") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            DetailScreen(
                                animeID = it.arguments?.getInt("mal_id") ?: 0,
                                navHostController = navHostController
                            )
                        }
                    }
                }
            }
        }
    }
}
