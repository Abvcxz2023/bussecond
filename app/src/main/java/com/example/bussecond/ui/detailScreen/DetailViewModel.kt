package com.example.bussecond.ui.detailScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bussecond.data.EtaRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface DetailUiState{
    data class Success(val etaList: List<readyToDisplay2>): DetailUiState
    object Loading: DetailUiState
    object Error: DetailUiState
}

const val TAG = "DetailViewModel"
class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val etaRepository: EtaRepository
): ViewModel() {
    val route: String = checkNotNull(savedStateHandle[DetailScreenDestination.routeIdArg])
    val bound: String = checkNotNull(savedStateHandle[DetailScreenDestination.boundArg])
    val destination: String = checkNotNull(savedStateHandle[DetailScreenDestination.destination])
    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    init {
        getRouteEta()
        Log.d(TAG,"initialize")
    }
    fun getRouteEta() {
        viewModelScope.launch {
            try {
                val (etaList, stopList) = etaRepository.getRouteEtaData(route,bound)
                val myTry = etaList.map {
                    readyToDisplay(
                        seq = it.seq,
                        etaSequence = it.etaSequence,
                        eta = it.eta?:"-",
                        stopName = stopList[it.seq-1]
                    )
                }.groupBy {
                    it.seq
                }.map {
                    readyToDisplay2(
                        seq = it.key,
                        stopName = it.value[0].stopName,
                        eta = List(it.value.size) { index -> it.value[index].eta }
                    )
                }
                detailUiState = DetailUiState.Success(etaList = myTry)
            }catch (e: IOException){
                detailUiState = DetailUiState.Error
            }
        }
    }
}

data class readyToDisplay(
    val seq: Int = 0, //The stop sequence number of a bus route
    val etaSequence: Int = 0,//The sequence number of ETA
    val stopName: String = "",
    val eta: String = "",//The timestamp of the next ETA
)

data class readyToDisplay2(
    val seq: Int = 0, //The stop sequence number of a bus route
    val stopName: String = "",
    val eta: List<String> = listOf(),//The timestamp of the next ETA
)