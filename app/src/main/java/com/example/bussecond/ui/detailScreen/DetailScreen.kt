package com.example.bussecond.ui.detailScreen

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bussecond.R
import com.example.bussecond.ui.AppViewModelProvider
import com.example.bussecond.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

object DetailScreenDestination: NavigationDestination{
    override val route = "item_details"
    const val boundArg = "boundId"
    const val routeIdArg = "routeId"
    const val destination = "destination"
    val routeWithArgs = "$route/{$boundArg}/{$routeIdArg}/{$destination}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    detailViewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {DetailTopBar(route = detailViewModel.route,
            destination = detailViewModel.destination,
            canNavigateBack = canNavigateBack,
            onLikeClick = {
            },
            navigateUp = navigateUp)}
    ) {
        innerPadding ->
        when(detailViewModel.detailUiState) {
            is DetailUiState.Success -> SuccessLoadScreen(routeEtaListData = (detailViewModel.detailUiState as DetailUiState.Success).etaList,
                onSaveClick = {
                    seq, stopname, stopId ->
                              scope.launch {
                                  detailViewModel.addBookmark(seq,stopname,stopId)
                              }
                },
                modifier = Modifier.padding(innerPadding))
            is DetailUiState.Loading -> LoadingScreen(modifier = Modifier.padding(innerPadding))
            is DetailUiState.Error -> ErrorScreen(retryAction = detailViewModel::getRouteEta, modifier = Modifier.padding(innerPadding))
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(route: String,
                 destination: String,
                 canNavigateBack: Boolean,
                 onLikeClick: () -> Unit,
                 navigateUp: () -> Unit) {
    TopAppBar(title = { Text(text = "$route $destination") },
        navigationIcon = {if(canNavigateBack){
            IconButton(onClick = navigateUp) {
                Icon(Icons.Filled.ArrowBack,"back")
            }
        } },
        actions = {
//            IconButton(onClick = { onLikeClick() }) {
//                Icon(Icons.Filled.ThumbUp,"ThumbUP")
//            }
        }
    )
}
@Composable
fun SuccessLoadScreen(routeEtaListData: List<DetailEtaItem>,
                      onSaveClick: (Int, String, String) -> Unit,
                      modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(routeEtaListData){
            EtaItem(route = it.stopName, another = it.eta,
                { onSaveClick(it.seq, it.stopName, it.stopId) }
            )
        }
    }
}

@Composable
fun EtaItem(route: String, another: List<String>,
            onSaveClick: () -> Unit,
            modifier: Modifier = Modifier) {
    var expanded by remember{ mutableStateOf(false)}
        Row(modifier = modifier
            .fillMaxSize()
            .animateContentSize()
            .clickable {
                expanded = !expanded
            }) {
            Text(text = route,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                maxLines = if(!expanded) 1 else Int.MAX_VALUE,
                modifier = Modifier
                    .width(180.dp)
                    .padding(horizontal = 5.dp),
                style = MaterialTheme.typography.headlineLarge)
            Text(text = "往", style = MaterialTheme.typography.titleLarge)
            if(expanded){
                Column(Modifier.wrapContentHeight()){
                    for(i in 0 until another.size){
                        Log.d("EtaItem",another[i])
                        Text(text = if(another[i] == "-") "-" else another[i].substring(11,19),
                            modifier = Modifier.padding(bottom = 5.dp),
                            style = MaterialTheme.typography.headlineLarge)
                    }
                }
            }else{
                if (another.isNotEmpty()){
                    Text(text = if(another[0] == "-") "-" else another[0].substring(11,19),
                        modifier = Modifier.padding(bottom = 5.dp),
                        style = MaterialTheme.typography.headlineLarge)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                onSaveClick()
            }) {
                Icon(imageVector = Icons.Default.Menu,
                    contentDescription = "Expanded")
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

@Composable
fun ItemDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card {
            Button(onClick = { onDismissRequest() }) {
                Text(text = "Cancel")
            }
            Column {
                Text("One")
                Text(text = "Two")
                Text(text = "Three")
            }
        }
    }
}

fun getTimeDiff(eta: String) : String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    val date: Date = format.parse(eta)
    val diff = date.getTime() - Date().getTime()
    val min_diff = diff/1000/60
    if(min_diff <= 0.01){
        return "-"
    }
    else return min_diff.toString()
}
