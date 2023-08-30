package com.example.bussecond.data

import android.util.Log
import com.example.bussecond.model.Eta
import com.example.bussecond.model.RouteDataList
import com.example.bussecond.model.RouteStationList
import com.example.bussecond.model.StopEtaList
import com.example.bussecond.model.StopInformation
import com.example.bussecond.model.StopInformationList
import com.example.bussecond.network.EtaApiService
import kotlinx.coroutines.flow.Flow

interface Repository{
    suspend fun getRouteEtaData(route:String, bound: String): Pair<List<Eta>,List<Pair<String,String>>>
    suspend fun getRouteStation(route: String, bound :String): RouteStationList
    suspend fun getAllStationInfo() : StopInformationList
    suspend fun getRouteListData() : RouteDataList
    suspend fun getStopETA(stop_id: String, route: String): StopEtaList

    //Database
    suspend fun insert(bookmark: Bookmark)
    suspend fun update(bookmark: Bookmark)
    suspend fun delete(bookmark: Bookmark)
    fun getBookmark(route: String, bound: String, seq: Int): Flow<Bookmark>
    fun getAllBookmark(): Flow<List<Bookmark>>
    suspend fun deleteBookmark(route: String, bound: String, seq: Int)
}

const val TAG = "EtaRepository"

class EtaRepository(private val etaApiService: EtaApiService,
    private val bookmarkDao: BookmarkDao
): Repository {
    var myStop: List<StopInformation> = listOf()
    override suspend fun getRouteEtaData(route: String, bound: String): Pair<List<Eta>,List<Pair<String,String>>> {
        val myList = mutableListOf<Pair<String,String>>()
        val stopIdList = getRouteStation(route = route,bound = when(bound){
            "O" -> "outbound"
            "I" -> "inbound"
            else -> "inbound"
        })
        stopIdList.data.forEach { current ->
            myList.add(
                Pair(current.stop,
                myStop.find {
                    it.stop == current.stop
                }?.name_tc ?: "None"
                )
            )
        }
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

    override suspend fun insert(bookmark: Bookmark) {
        bookmarkDao.insert(bookmark)
    }

    override suspend fun update(bookmark: Bookmark) {
        bookmarkDao.update(bookmark)
    }

    override suspend fun delete(bookmark: Bookmark) {
        bookmarkDao.delete(bookmark)
    }

    override fun getBookmark(route: String, bound: String, seq: Int): Flow<Bookmark> {
        return bookmarkDao.getBookmark(route,bound, seq)
    }

    override fun getAllBookmark(): Flow<List<Bookmark>> {
        return bookmarkDao.getAllBookmark()
    }

    override suspend fun deleteBookmark(route: String, bound: String, seq: Int) {
        bookmarkDao.deleteBookmark(route, bound, seq)
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
