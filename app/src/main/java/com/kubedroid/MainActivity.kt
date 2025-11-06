package com.kubedroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kubedroid.ui.navigation.Screen
import com.kubedroid.ui.screens.*
import com.kubedroid.ui.theme.KubeDroidTheme
import com.kubedroid.ui.viewmodel.ClusterViewModel
import com.kubedroid.ui.viewmodel.PodDetailViewModel
import com.kubedroid.ui.viewmodel.ResourceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KubeDroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    KubeDroidApp()
                }
            }
        }
    }
}

@Composable
fun KubeDroidApp() {
    val navController = rememberNavController()
    val clusterViewModel: ClusterViewModel = viewModel()
    val clusterUiState by clusterViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Clusters.route
    ) {
        // Cluster List Screen
        composable(Screen.Clusters.route) {
            ClusterListScreen(
                uiState = clusterUiState,
                onAddCluster = { cluster ->
                    clusterViewModel.addCluster(cluster)
                },
                onConnectCluster = { cluster ->
                    clusterViewModel.connectToCluster(cluster)
                },
                onRemoveCluster = { clusterId ->
                    clusterViewModel.removeCluster(clusterId)
                },
                onNavigateToResources = {
                    navController.navigate(Screen.Workloads.route)
                }
            )
        }

        // Resource Browser Screen
        composable(Screen.Workloads.route) {
            ResourceBrowserScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCategory = { category ->
                    when (category) {
                        "pods" -> navController.navigate(Screen.Pods.route)
                        "deployments" -> navController.navigate(Screen.Deployments.route)
                        "services" -> navController.navigate(Screen.Services.route)
                    }
                }
            )
        }

        // Pods Screen
        composable(Screen.Pods.route) {
            val resourceViewModel = remember {
                ResourceViewModel(clusterViewModel.getClientManager())
            }
            val uiState by resourceViewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                resourceViewModel.loadPods()
            }

            PodsScreen(
                uiState = uiState,
                onRefresh = {
                    resourceViewModel.loadPods()
                },
                onPodClick = { namespace, podName ->
                    navController.navigate(Screen.PodDetail.createRoute(namespace, podName))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Pod Detail Screen
        composable(
            route = Screen.PodDetail.route,
            arguments = listOf(
                navArgument("namespace") { type = NavType.StringType },
                navArgument("podName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val namespace = backStackEntry.arguments?.getString("namespace") ?: ""
            val podName = backStackEntry.arguments?.getString("podName") ?: ""

            val podDetailViewModel = remember {
                PodDetailViewModel(clusterViewModel.getClientManager())
            }
            val uiState by podDetailViewModel.uiState.collectAsState()

            PodDetailScreen(
                namespace = namespace,
                podName = podName,
                uiState = uiState,
                onLoadLogs = {
                    podDetailViewModel.loadLogs(namespace, podName)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Deployments Screen
        composable(Screen.Deployments.route) {
            val resourceViewModel = remember {
                ResourceViewModel(clusterViewModel.getClientManager())
            }
            val uiState by resourceViewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                resourceViewModel.loadDeployments()
            }

            DeploymentsScreen(
                uiState = uiState,
                onRefresh = {
                    resourceViewModel.loadDeployments()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Services Screen
        composable(Screen.Services.route) {
            val resourceViewModel = remember {
                ResourceViewModel(clusterViewModel.getClientManager())
            }
            val uiState by resourceViewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                resourceViewModel.loadServices()
            }

            ServicesScreen(
                uiState = uiState,
                onRefresh = {
                    resourceViewModel.loadServices()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
