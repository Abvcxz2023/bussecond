package com.example.bussecond.data

import com.example.bussecond.model.Eta
import com.example.bussecond.model.RouteEta
import com.example.bussecond.model.RouteDataList
import com.example.bussecond.model.RouteStationList
import com.example.bussecond.model.StopEtaList
import com.example.bussecond.model.StopInformation
import com.example.bussecond.model.StopInformationList
import com.example.bussecond.network.EtaApiService

interface Repository{
    suspend fun getRouteEtaData(route:String, bound: String): List<Eta>
    suspend fun getRouteStation(route: String, bound :String): RouteStationList
    suspend fun getAllStationInfo() : StopInformationList
    suspend fun getRouteListData() : RouteDataList
    suspend fun getStopETA(stop_id: String, route: String): StopEtaList
}

class EtaRepository(private val etaApiService: EtaApiService): Repository {
    init {

    }
    override suspend fun getRouteEtaData(route: String, bound: String): List<Eta> {
        return etaApiService.getRouteEta(route).data.filter {
            it.dir == bound
        }
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
}
