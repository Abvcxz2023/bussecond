package com.example.bussecond

import android.app.Application
import com.example.bussecond.data.DefaultAppContainer



class BusSecondApplication:Application() {
    lateinit var container: DefaultAppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}