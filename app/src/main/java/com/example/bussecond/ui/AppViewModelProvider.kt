package com.example.bussecond.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bussecond.BusSecondApplication
import com.example.bussecond.ui.bookmark.BookmarkScreenViewModel
import com.example.bussecond.ui.detailScreen.DetailViewModel
import com.example.bussecond.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            HomeViewModel(etaRepository = busSecondApplication().container.etaRepository)
        }
        initializer {
            DetailViewModel(
                this.createSavedStateHandle(),
                etaRepository = busSecondApplication().container.etaRepository
            )
        }
        initializer {
            BookmarkScreenViewModel(
                etaRepository = busSecondApplication().container.etaRepository
            )
        }
    }

}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.busSecondApplication(): BusSecondApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BusSecondApplication)
