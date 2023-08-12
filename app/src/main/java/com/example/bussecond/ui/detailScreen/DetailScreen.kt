package com.example.bussecond.ui.detailScreen

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bussecond.R
import com.example.bussecond.model.Eta
import com.example.bussecond.ui.AppViewModelProvider
import com.example.bussecond.ui.navigation.NavigationDestination

object DetailScreenDesination: NavigationDestination{
    override val route = "item_details"
    override val titleRes = 1
    const val boundArg = "itemId"
    const val routeIdArg = "routeId"
    val routeWithArgs = "$route/{$boundArg}{$routeIdArg}"
}

@Composable
fun DetailScreen(retryAction: () -> Unit,
                 detailViewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    when(detailViewModel.detailUiState) {
        is DetailUiState.Success -> SuccessLoadScreen(routeEtaListData = (detailViewModel.detailUiState as DetailUiState.Success).etaList)
        is DetailUiState.Loading -> LoadingScreen()
        else -> {}
    }
}
@Composable
fun SuccessLoadScreen(routeEtaListData: List<Eta>) {
    LazyColumn() {
        items(routeEtaListData.filter {
            it.etaSequence == 1
        }){
            etaItem(route = it.route, another = it.etaSequence.toString())
        }
    }
}

@Composable
fun etaItem(route: String, another: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.fillMaxSize()) {
            Text(text = route,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 5.dp),
                style = MaterialTheme.typography.headlineLarge)

            Text(text = "往", style = MaterialTheme.typography.titleLarge)
            Text(text = another,
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
