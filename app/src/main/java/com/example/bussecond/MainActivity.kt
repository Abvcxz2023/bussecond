package com.example.bussecond

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.bussecond.ui.bookmark.BookmarkScreen
import com.example.bussecond.ui.navigation.MyEtaApp
import com.example.bussecond.ui.theme.BussecondTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

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
                        MyEtaApp()
//                    val singapore = LatLng(22.340879, 114.201550)
//                    val cameraPositionState = rememberCameraPositionState {
//                        position = CameraPosition.fromLatLngZoom(singapore, 10f)
//                    }
//                    GoogleMap(
//                        modifier = Modifier.fillMaxSize(),
//                        cameraPositionState = cameraPositionState
//                    ) {
//                        Marker(
//                            state = MarkerState(position = singapore),
//                            title = "Singapore",
//                            snippet = "Marker in Singapore"
//                        )
//                    }
                    //BookmarkScreen()
                }
            }
        }
    }
}

