package com.example.bussecond.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bussecond.data.EtaRepository
import com.example.bussecond.model.RouteDataList
import com.example.bussecond.model.StopInformationList
import kotlinx.coroutines.launch

const val TAG = "HomeViewModel"
sealed interface HomeUiState{
    data class Success(val routeDataList: RouteDataList): HomeUiState
    object Loading: HomeUiState
    object Error: HomeUiState
}
class HomeViewModel(private val etaRepository: EtaRepository): ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    var searchText: String by mutableStateOf("")
        private set

    var allStopInformation: StopInformationList =
        StopInformationList("", "", "", emptyList())
    init {
        Log.d(TAG,"initialize")
        getRouteListDataForTheHomeScreen()
    }

    lateinit var route: String
    lateinit var bound: String

    fun onSearchTextChanged(searchText: String){
        this.searchText = searchText
    }

    fun getRouteListDataForTheHomeScreen(){
        Log.d(TAG,"getRouteListData")
        viewModelScope.launch {
            try {
                etaRepository.getMyStop()
                homeUiState = HomeUiState.Success(etaRepository.getRouteListData())
            }catch (e: java.io.IOException){
                homeUiState = HomeUiState.Error
            }
            catch (e: retrofit2.HttpException){
                homeUiState = HomeUiState.Error
                Log.e("HomeViewModel",e.message())
            }
        }
    }
}