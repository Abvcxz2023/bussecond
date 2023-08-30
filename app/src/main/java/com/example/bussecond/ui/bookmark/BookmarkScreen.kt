package com.example.bussecond.ui.bookmark

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bussecond.R
import com.example.bussecond.data.Bookmark
import com.example.bussecond.model.StopEta
import com.example.bussecond.ui.AppViewModelProvider
import com.example.bussecond.ui.navigation.NavigationDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object BookmarkScreenDestination : NavigationDestination {
    override val route = "bookmark"
}

@Composable
fun MainBookmarkScreen(
    onNavigateBack: () -> Unit,
    bookmarkScreenViewModel: BookmarkScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val myUiState by bookmarkScreenViewModel.bookmarkListFromDB.collectAsState()
    LaunchedEffect(myUiState){
        bookmarkScreenViewModel.getAllBookmarkEta()
    }
    when(bookmarkScreenViewModel.bookmarkScreenUiState){
        is BookmarkScreenUiState.Success -> {
            BookmarkScreen(
                onNavigateBack = onNavigateBack,
                bookmarkScreenViewModel = bookmarkScreenViewModel)}
        is BookmarkScreenUiState.Loading -> {
            LoadingScreen()
        }
        is BookmarkScreenUiState.Error -> {
            ErrorScreen(
                retryAction = { CoroutineScope(Dispatchers.IO).launch {
                    bookmarkScreenViewModel.getAllBookmarkEta()
                } }
            )}
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    onNavigateBack: () -> Unit,
    bookmarkScreenViewModel: BookmarkScreenViewModel,
) {
    Log.d("BookmarkScreen","recomposite")
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Bookmark") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { bookmarkScreenViewModel },
                ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
        }
    ) {
        innerPadding ->
        val damn by bookmarkScreenViewModel.myOutputStateFlow.collectAsState()
        Log.d("MYList",damn.toString())
        Column(modifier = Modifier.padding(innerPadding)) {
            if(damn.isNullOrEmpty()){
                Box(modifier = Modifier.fillMaxSize()){
                    Text(text = "No Bookmark",
                        Modifier.align(Alignment.Center))
                }
            }else {
                LazyColumn {
                    items(damn){
                        MyTryItem(stopEtaList = it.first, it.second) { route, bound, seq ->
                            bookmarkScreenViewModel.deleteBookmark(
                                route,
                                bound,
                                seq
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyTryItem(stopEtaList: List<StopEta>,
              bookmark: Bookmark = Bookmark("","",0,"","",""),
              onBookmarkDelete: (String, String, Int) -> Unit) {
    var expanded by remember{ mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxSize()
        .animateContentSize()
        .clickable {
            expanded = !expanded
        }) {
        Row {
            Text(
                text = "${stopEtaList[0].route}@${bookmark.stopname}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .weight(9f)
                    .padding(top = 4.dp, start = 8.dp),
                maxLines = if (!expanded) 1 else Int.MAX_VALUE,
                overflow = TextOverflow.Ellipsis)
            IconButton(modifier = Modifier.weight(1f),onClick = {
                onBookmarkDelete(stopEtaList[0].route, stopEtaList[0].dir, stopEtaList[0].seq)
            }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Delete")
            }
        }
        Row {
            Text(
                text = "往${stopEtaList[0].dest_tc}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Column(Modifier.fillMaxSize()) {
                for (etaData in stopEtaList) {
                    val eta: String = if (etaData.eta == null) {
                        "-"
                    } else if (etaData.eta == "-") {
                        "-"
                    } else {
                        etaData.eta.substring(11, 19)
                    }
                    Text(text = eta, Modifier.align(Alignment.CenterHorizontally))
                }
            }
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
fun ErrorScreen(retryAction: () -> Unit = {}, modifier: Modifier = Modifier) {
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