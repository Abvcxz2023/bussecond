package com.example.bussecond.data

import android.util.Log
import com.example.bussecond.model.Eta
import com.example.bussecond.model.RouteEta
import com.example.bussecond.model.RouteDataList
import com.example.bussecond.model.RouteStationList
import com.example.bussecond.model.StopEtaList
import com.example.bussecond.model.StopInformation
import com.example.bussecond.model.StopInformationList
import com.example.bussecond.network.EtaApiService
import com.example.bussecond.ui.detailScreen.DetailEta
import com.example.bussecond.ui.home.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

interface Repository{
    suspend fun getRouteEtaData(route:String, bound: String): Pair<List<Eta>,List<String>>
    suspend fun getRouteStation(route: String, bound :String): RouteStationList
    suspend fun getAllStationInfo() : StopInformationList
    suspend fun getRouteListData() : RouteDataList
    suspend fun getStopETA(stop_id: String, route: String): StopEtaList
}

const val TAG = "EtaRepository"

class beautifiedEtaData() {

}

class EtaRepository(private val etaApiService: EtaApiService): Repository {
    var myStop: List<StopInformation> = listOf()
    override suspend fun getRouteEtaData(route: String, bound: String): Pair<List<Eta>,List<String>> {
        val myList = mutableListOf<String>()
        val temp = getRouteStation(route = route,bound = when(bound){
            "O" -> "outbound"
            "I" -> "inbound"
            else -> "inbound"
        })
        Log.d("getRouteEtaData",temp.data.toString())
        temp.data.forEach { current ->
            Log.d("getRouteEtaData",current.stop)
            myList.add(
                myStop.find {
                    it.stop == current.stop
                }?.name_tc ?: "None"
            )
        }
        Log.d("getRouteEtaData",myList.toString())

        return Pair(etaApiService.getRouteEta(route).data.filter {
            it.dir == bound
        },
            myList.toList()
        )
    }

    override suspend fun getRouteStation(route: String, bound: String): RouteStationList {
        return etaApiService.getRouteStation(route, bound)
    }

    override suspend fun getAllStationInfo(): StopInformationList {
        return etaApiService.getAllStationInfo()
    }

    override suspend fun getRouteListData(): RouteDataList {
        return etaApiService.getRouteListData()
    }

    override suspend fun getStopETA(stop_id: String, route: String): StopEtaList {
        return etaApiService.getStopETA(stop_id, route)
    }

    suspend fun getMyStop() {
        myStop = etaApiService.getAllStationInfo().data
        if(myStop.isNullOrEmpty()){
            Log.d("EtaRepository","Is Null")
        }
        else{
            Log.d("EtaRepository","Isn't")
        }
    }


}
