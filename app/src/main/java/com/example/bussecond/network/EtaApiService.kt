package com.example.bussecond.network
import com.example.bussecond.model.RouteEta
import com.example.bussecond.model.RouteDataList
import com.example.bussecond.model.RouteStationList
import com.example.bussecond.model.StopEtaList
import com.example.bussecond.model.StopInformationList
import retrofit2.http.GET
import retrofit2.http.Path

interface EtaApiService{
    @GET("v1/transport/kmb/route-eta/{route}/1")
    suspend fun getRouteEta(@Path("route") route:String ) : RouteEta

    @GET("v1/transport/kmb/route-stop/{route}/{bound}/1")
    suspend fun getRouteStation(@Path("route") route: String, @Path("bound") bound :String) : RouteStationList

    @GET("v1/transport/kmb/stop")
    suspend fun getAllStationInfo() : StopInformationList

    @GET("v1/transport/kmb/route/")
    suspend fun getRouteListData() : RouteDataList

    @GET("v1/transport/kmb/eta/{stop_id}/{route}/1")
    suspend fun getStopETA(@Path("stop_id") stop_id: String, @Path("route") route: String): StopEtaList
}
