package com.example.bussecond

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bussecond.data.DataSource
import com.example.bussecond.data.EtaHomePageItem
import com.example.bussecond.ui.theme.BussecondTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BussecondTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    Text(text = "Bye")
}

@Composable
fun etaItem(etaItem: EtaHomePageItem) {
        Row(modifier = Modifier.fillMaxSize()) {
            Text(text = "${etaItem.route}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier,
                style = MaterialTheme.typography.bodyMedium)

            Text(text = "往", style = MaterialTheme.typography.bodySmall)
            Text(text = "${etaItem.destination}",
                modifier = Modifier.padding(bottom = 5.dp),
                style = MaterialTheme.typography.bodyMedium)
        }
        Divider()
}

@Composable
fun etaItemList() {
    LazyColumn() {
        items(DataSource.etaHomePageSampleList){
            etaItem(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BussecondTheme {
        Greeting("Android")
    }
}

@Preview(showBackground = true)
@Composable
fun etaHomePageItemListPreview() {
    etaItemList()
}

@Preview(showBackground = true)
@Composable
fun etaItemPreview() {
    etaItem(EtaHomePageItem("91M","鑽石山站"))
}