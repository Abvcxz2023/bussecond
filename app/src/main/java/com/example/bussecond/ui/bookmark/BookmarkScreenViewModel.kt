package com.example.bussecond.ui.bookmark

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bussecond.data.Bookmark
import com.example.bussecond.data.EtaRepository
import com.example.bussecond.model.StopEta
import com.example.bussecond.model.StopEtaList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
sealed interface BookmarkScreenUiState{
    data class Success(val myList: List<Pair<List<StopEta>,Bookmark>>): BookmarkScreenUiState
    object Loading: BookmarkScreenUiState
    object Error: BookmarkScreenUiState
}

data class BookmarkUiState(val bookmarkList: List<Bookmark>)
class BookmarkScreenViewModel(
    private val etaRepository: EtaRepository
) : ViewModel() {
    val bookmarkListFromDB: StateFlow<BookmarkUiState> = etaRepository.getAllBookmark().filterNotNull().map {
        BookmarkUiState(it)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = BookmarkUiState(emptyList())
    )

    private val _myOutputStateFlow = MutableStateFlow(mutableListOf<Pair<List<StopEta>,Bookmark>>())
    val myOutputStateFlow = _myOutputStateFlow.asStateFlow()
    var bookmarkScreenUiState: BookmarkScreenUiState by mutableStateOf(BookmarkScreenUiState.Loading)
        private set

    suspend fun getAllBookmarkEta() {
        bookmarkScreenUiState = BookmarkScreenUiState.Loading
            try {
                _myOutputStateFlow.value = mutableListOf()
                coroutineScope {
                    bookmarkListFromDB.value.bookmarkList.forEach {
                            bookmark ->
                            this.launch {
                                val result = etaRepository.getStopETA(bookmark.stopId,bookmark.route)
                                val newResult = result.copy(
                                    data = result.data.filter {
                                        stopEta ->
                                        stopEta.dir == bookmark.bound
                                    }
                                )
                                _myOutputStateFlow.value.add(Pair(newResult.data,bookmark))
                            }
                    }
                }
                _myOutputStateFlow.value.sortBy {
                    it.first[0].route
                }
                bookmarkScreenUiState = BookmarkScreenUiState.Success(_myOutputStateFlow.value)
            }catch (e: Exception){
                bookmarkScreenUiState = BookmarkScreenUiState.Error
            }
    }

    fun deleteBookmark(route: String, bound: String, seq: Int) {
        Log.d("deleteBookmark","Deleting bookmark: $route $bound ${seq.toString()}")
        viewModelScope.launch {
            etaRepository.deleteBookmark(route, bound, seq)
        }
    }



}