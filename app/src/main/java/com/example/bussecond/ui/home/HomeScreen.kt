package com.example.bussecond.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bussecond.data.EtaHomePageItem
import com.example.bussecond.R
import com.example.bussecond.model.RouteDataList
import com.example.bussecond.ui.AppViewModelProvider
import com.example.bussecond.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}
@Composable
fun EtaHomeScreen(onRouteItemClick: (String, String) -> Unit,
                  homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    etaItemList(homeUiState = homeViewModel.homeUiState, retryAction = homeViewModel::getRouteListDataForTheHomeScreen,
        onRouteItemClick = onRouteItemClick
    )
}
@Composable
fun etaItemList(homeUiState: HomeUiState, retryAction: () -> Unit, onRouteItemClick: (String, String) -> Unit) {
    when(homeUiState){
        is HomeUiState.Error -> ErrorScreen(retryAction = retryAction)
        is HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Success -> SuccessLoadScreen(homeUiState.routeDataList,onRouteItemClick)
    }
}
@Composable
fun SuccessLoadScreen(routeDataList: RouteDataList, onRouteItemClick: (String, String) -> Unit) {
    val resultList = routeDataList.data.map{
        EtaHomePageItem(route = it.route, destination = it.dest_tc, bound = it.bound)
    }
    LazyColumn() {
        items(resultList){
                etaItem(it, modifier = Modifier.padding(vertical = 5.dp).clickable {
                    onRouteItemClick(it.route,it.bound)
                })
        }
    }
}

@Composable
fun etaItem(etaItem: EtaHomePageItem, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            Text(text = "${etaItem.route}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 5.dp),
                style = MaterialTheme.typography.headlineLarge)

            Text(text = "往", style = MaterialTheme.typography.titleLarge)
            Text(text = "${etaItem.destination}",
                modifier = Modifier.padding(bottom = 5.dp),
                style = MaterialTheme.typography.headlineLarge)
        }
    }
    Divider()
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = "loading"
    )
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
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

@Preview(showBackground = true)
@Composable
fun etaHomePageItemListPreview() {
    //etaItemList()
}

@Preview(showBackground = true)
@Composable
fun etaItemPreview() {
    etaItem(EtaHomePageItem("91M","鑽石山站", bound = "O"))
}