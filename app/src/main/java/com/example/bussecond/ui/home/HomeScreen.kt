package com.example.bussecond.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bussecond.data.EtaHomePageItem
import com.example.bussecond.R
import com.example.bussecond.model.RouteData
import com.example.bussecond.ui.AppViewModelProvider
import com.example.bussecond.ui.navigation.NavigationDestination

object HomeScreenDestination : NavigationDestination {
    override val route = "home"
    val titleRes = R.string.welcome
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EtaHomeScreen(onRouteItemClick: (String, String, String) -> Unit,
                  onBookmarkScreenClick: () -> Unit,
                  homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            val searchText = homeViewModel.searchText.collectAsState()
            DamnTopBar(searchText = searchText.value,
                onSearchTextChange = homeViewModel::onSearchTextChanged,
                onSearchTextClear = homeViewModel::onSearchTextClear,
                onBookmarkScreenClick = onBookmarkScreenClick)
//            val searchText = homeViewModel.searchText.collectAsState()
//            Log.d("TopBar",searchText.value)
//            EtaTopBar(searchText.value,homeViewModel::onSearchTextChanged, homeViewModel::onSearchTextClear)
        }
    ) {
        Column {
//            val searchText = homeViewModel.searchText.collectAsState()
//            DamnTopBar(searchText = searchText.value, onSearchTextChange = homeViewModel::onSearchTextChanged, onSearchTextClear = homeViewModel::onSearchTextClear)
            val routeDataList = homeViewModel.routeDataList.collectAsState()
            EtaItemList(homeUiState = homeViewModel.homeUiState,
                retryAction = homeViewModel::getRouteListDataForTheHomeScreen,
                onRouteItemClick = onRouteItemClick,
                routeDataList = routeDataList.value,
                modifier = Modifier.padding(it)
            )
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EtaTopBar(searchText: String, onSearchTextChange: (String) -> Unit, onSearchTextClear: () -> Unit, modifier: Modifier = Modifier) {
    val trailingIconView = @Composable {
        IconButton(
            onClick = onSearchTextClear,
        ) {
            Icon(
                Icons.Default.Clear,
                contentDescription = "",
                tint = Color.Black
            )
        }
    }
    TopAppBar(
        title = {
            Log.d("INside",searchText)
                TextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    trailingIcon = if(searchText.isNotEmpty()) trailingIconView else null,
                    modifier = Modifier
                        .fillMaxSize(),
                    label = { Text(text = searchText)},
                    )
        },
        modifier
            .fillMaxWidth()
            .background(Color.Red),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DamnTopBar(searchText: String, onSearchTextChange: (String) -> Unit,
               onSearchTextClear: () -> Unit,
               onBookmarkScreenClick: () -> Unit,
               modifier: Modifier = Modifier) {
    Row {
        IconButton(onClick = onBookmarkScreenClick) {
            Icon(imageVector = Icons.Default.Info, contentDescription = "bookmark")
        }
        TextField(value = searchText, onValueChange = onSearchTextChange, trailingIcon = {
            if (!searchText.isBlank()){
                IconButton(onClick = onSearchTextClear) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search")})
    }

}
@Composable
fun EtaItemList(homeUiState: HomeUiState,
                routeDataList: List<RouteData>,
                retryAction: () -> Unit,
                onRouteItemClick: (String, String, String) -> Unit,
                modifier: Modifier = Modifier) {
    when(homeUiState){
        is HomeUiState.Error -> ErrorScreen(
            retryAction = retryAction,
            modifier = modifier)
        is HomeUiState.Loading -> LoadingScreen(modifier = modifier)
        is HomeUiState.Success -> SuccessLoadScreen(routeDataList,onRouteItemClick,modifier)
    }
}
@Composable
fun SuccessLoadScreen(
    routeDataList: List<RouteData>,
    onRouteItemClick: (String, String, String) -> Unit,
    modifier: Modifier = Modifier) {
    val resultList = routeDataList.filter {
        it.service_type == "1"
    }.map{
        EtaHomePageItem(route = it.route, destination = it.dest_tc, bound = it.bound)
    }
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(resultList){
                EtaItem(it, modifier = Modifier
                    .padding(vertical = 5.dp)
                    .clickable {
                        onRouteItemClick(it.bound, it.route, it.destination)
                    })
        }
    }
}

@Composable
fun EtaItem(etaItem: EtaHomePageItem, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            Text(text = etaItem.route,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 5.dp),
                style = MaterialTheme.typography.headlineLarge)

            Text(text = "往", style = MaterialTheme.typography.titleLarge)
            Text(text = etaItem.destination,
                modifier = Modifier.padding(bottom = 5.dp),
                style = MaterialTheme.typography.headlineLarge)
        }
    }
    Divider()
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.fillMaxSize(),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = "loading",
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = "load failed", modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(text = "retry")
        }
    }
}
