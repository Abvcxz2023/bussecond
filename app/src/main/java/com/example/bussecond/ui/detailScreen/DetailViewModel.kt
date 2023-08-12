package com.example.bussecond.ui.detailScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bussecond.data.EtaRepository
import com.example.bussecond.model.Eta
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface DetailUiState{
    data class Success(val etaList: List<Eta>): DetailUiState
    object Loading: DetailUiState
    object Error: DetailUiState
}

const val TAG = "DetailViewModel"
class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val etaRepository: EtaRepository
): ViewModel() {
    val route: String = checkNotNull(savedStateHandle[DetailScreenDesination.routeIdArg])
    val bound: String = checkNotNull(savedStateHandle[DetailScreenDesination.boundArg])
    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    init {
        getRouteEta()
        Log.d(TAG,"initialize")
    }
    fun getRouteEta() {
        viewModelScope.launch {
            try {
                detailUiState = DetailUiState.Success(etaRepository.getRouteEtaData(route,bound))
            }catch (e: IOException){
                detailUiState = DetailUiState.Error
            }
        }
    }
}