package com.example.bussecond.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bussecond.ui.detailScreen.DetailScreen
import com.example.bussecond.ui.detailScreen.DetailScreenDesination
import com.example.bussecond.ui.home.EtaHomeScreen
import com.example.bussecond.ui.home.HomeDestination


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEtaAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = "Try") },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back button"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEtaApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            MyEtaAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = HomeDestination.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = HomeDestination.route){
                EtaHomeScreen(
                    onRouteItemClick = {
                        route: String, bound: String ->
                        navController.navigate("${DetailScreenDesination.route}/${bound}${route}")
                    }
                )
            }
            composable(route = DetailScreenDesination.routeWithArgs,
                arguments = listOf(navArgument(DetailScreenDesination.boundArg){ type = NavType.StringType},
                        navArgument(DetailScreenDesination.routeIdArg){ type = NavType.StringType}
                    )
            ){
                DetailScreen(retryAction = {  })
            }
        }
    }
}