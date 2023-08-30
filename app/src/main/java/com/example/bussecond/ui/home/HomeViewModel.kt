package com.example.bussecond.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bussecond.data.EtaRepository
import com.example.bussecond.model.RouteData
import com.example.bussecond.model.RouteDataList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

const val TAG = "HomeViewModel"
sealed interface HomeUiState{
    object Success: HomeUiState
    object Loading: HomeUiState
    object Error: HomeUiState
}
class HomeViewModel(private val etaRepository: EtaRepository): ViewModel() {
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _routeDataList = MutableStateFlow<RouteDataList>(
        RouteDataList(
            data = listOf()
        )
    )
    val routeDataList: StateFlow<List<RouteData>> = _searchText.combine(
        _routeDataList
    ) {
         text, routeData,  ->
        if(text.isBlank()){
            routeData.data
        }else {
            routeData.data.filter {
                it.matchRoute(text)
            }
        }
    }.stateIn(scope = viewModelScope,
        initialValue = listOf<RouteData>(),
        started = SharingStarted.WhileSubscribed(5000)
        )

    init {
        getRouteListDataForTheHomeScreen()
    }

    lateinit var route: String
    lateinit var bound: String

    fun onSearchTextChanged(searchText: String) {
        this._searchText.value = searchText
    }

    fun onSearchTextClear() {
        this._searchText.value = ""
    }



    fun getRouteListDataForTheHomeScreen(){
        Log.d(TAG,"getRouteListData")
        viewModelScope.launch {
            try {
                etaRepository.getMyStop()
                _routeDataList.value = etaRepository.getRouteListData()
                homeUiState = HomeUiState.Success
            }catch (e: java.io.IOException){
                homeUiState = HomeUiState.Error
            }
            catch (e: retrofit2.HttpException){
                homeUiState = HomeUiState.Error
            }
        }
    }

    fun RouteData.matchRoute(query: String): Boolean {
        return this.route.contains(query, ignoreCase = true)
    }
}