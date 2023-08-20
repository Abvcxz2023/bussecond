package com.example.bussecond.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bussecond.ui.AppViewModelProvider
import com.example.bussecond.ui.detailScreen.DetailScreen
import com.example.bussecond.ui.detailScreen.DetailScreenDestination
import com.example.bussecond.ui.home.EtaHomeScreen
import com.example.bussecond.ui.home.HomeScreenDestination
import com.example.bussecond.ui.home.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEtaAppBar(
    canNavigateBack: Boolean,
    backStackEntry: NavBackStackEntry?,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
                    if(backStackEntry?.destination?.route == DetailScreenDestination.routeWithArgs){
                        Row(){
                            Text(text = backStackEntry?.arguments?.getString("routeId")?: "")
                            Text(text = " 往 ")
                            Text(text = backStackEntry?.arguments?.getString("destination")?: "", modifier)
                        }
                    }
                    else if(backStackEntry?.destination?.route == HomeScreenDestination.route) {
                        TextField(value = "searchText", onValueChange = {},
                            placeholder = { Text(text = "Search")},
                            maxLines = 1)
                    }
                    else{
                        Text(text = "Welcome")
                    }
                },
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
                backStackEntry = backStackEntry,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = HomeScreenDestination.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(route = HomeScreenDestination.route){
                EtaHomeScreen(
                    onRouteItemClick = {
                        bound: String, route: String, destination: String ->
                        navController.navigate("${DetailScreenDestination.route}/${bound}/${route}/${destination}")
                        Log.d("navigationGraph",route)
                    },
                    homeViewModel  = viewModel(factory = AppViewModelProvider.Factory)
                )
            }
            composable(route = DetailScreenDestination.routeWithArgs,
                arguments = listOf(navArgument(DetailScreenDestination.boundArg){ type = NavType.StringType},
                        navArgument(DetailScreenDestination.routeIdArg){ type = NavType.StringType},
                        navArgument(DetailScreenDestination.destination) {type = NavType.StringType}
                    )
            ){
                Log.d("navigationGraph",backStackEntry?.arguments?.getString("destination")?: "Damn")
                DetailScreen()
            }
        }
    }
}