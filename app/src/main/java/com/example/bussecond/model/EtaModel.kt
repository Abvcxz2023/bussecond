package com.example.bussecond.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RouteEta(
    val type: String = "", //The corresponding API that returns the data
    val version: String = "",//The version number of the JSON returned.
    val generated_timestamp: String = "",//The timestamp of the initial generated time of the response before it is cached.
    val data: List<Eta> = emptyList(),
)


@Serializable
data class Eta(
    val co: String = "KMB",//The bus company
    val route: String = "", //The bus route number of the requested bus company
    val dir: String = "",//The direction of the bus route
    @SerialName("service_type") val serviceType: Int = 0,//The service type of the bus route.
    val seq: Int = 0, //The stop sequence number of a bus route
    @SerialName("dest_tc") val destinationTc: String = "",//The destination of a bus route in Traditional Chinese
    @SerialName("dest_sc") val destinationSc: String = "",//The destination of a bus route in Simplified Chinese.
    @SerialName("dest_en") val destinationEn: String = "",//The destination of a bus route in English
    @SerialName("eta_seq") val etaSequence: Int = 0,//The sequence number of ETA
    val eta: String? = "-",//The timestamp of the next ETA
    @SerialName("rmk_tc") val remarkTc: String = "",//The remark of an ETA in Traditional Chinese
    @SerialName("rmk_sc") val remarkSc: String = "",//The remark of an ETA in Simplified Chinese.
    @SerialName("rmk_en") val remarkEn: String = "",//The remark of an ETA in English
    @SerialName("data_timestamp") val dataTimestamp: String = "",//The timestamp of the data when it was initially
)

@Serializable
data class RouteStationList(
    val type: String = "", //The corresponding API that returns the data
    val version: String = "",//The version number of the JSON returned.
    val generated_timestamp: String = "",//The timestamp of the initial generated time of the response before it is cached.
    val data: List<RouteStation> = emptyList(),
)

@Serializable
data class RouteStation(
    val route: String = "",
    val bound: String = "",
    val service_type: String = "",
    val seq: String = "",
    val stop: String = "",
)

@Serializable
data class StopInformationList(
    val type: String,
    val version: String,
    val generated_timestamp: String,
    val data: List<StopInformation>,
)

@Serializable
data class StopInformation(
    val stop: String = "",
    val name_en: String = "",
    val name_tc: String = "",
    val name_sc: String = "",
    val lat: String = "",
    val long: String = "",
)

@Serializable
data class RouteDataList(
    val type: String = "", //The corresponding API that returns the data
    val version: String = "",//The version number of the JSON returned.
    val generated_timestamp: String = "",//The timestamp of the initial generated time of the response before it is cached.
    val data : List<RouteData>
)

@Serializable
data class RouteData(
    val route: String = "",
    val bound: String = "",
    val service_type: String = "",
    val orig_en: String = "",
    val orig_tc: String = "",
    val orig_sc: String = "",
    val dest_en: String = "",
    val dest_tc: String = "",
    val dest_sc: String = "",
)

@Serializable
data class StopEtaList(
    val type: String,
    val version: String,
    val generated_timestamp: String,
    val data: List<StopEta>
)

@Serializable
data class StopEta(
    val co: String = "KMB",//The bus company
    val route: String = "", //The bus route number of the requested bus company
    val dir: String = "",//The direction of the bus route
    val service_type: Int = 0,//The service type of the bus route.
    val seq: Int = 0, //The stop sequence number of a bus route
    val stop: String = "",
    val dest_tc: String = "",//The destination of a bus route in Traditional Chinese
    val dest_sc: String = "",//The destination of a bus route in Simplified Chinese.
    val dest_en: String = "",//The destination of a bus route in English
    val eta_seq: Int = 0,//The sequence number of ETA
    val eta: String? = "-",//The timestamp of the next ETA
    val rmk_tc: String = "",//The remark of an ETA in Traditional Chinese
    val rmk_sc: String = "",//The remark of an ETA in Simplified Chinese.
    val rmk_en: String = "",//The remark of an ETA in English
    val data_timestamp: String = "",//The timestamp of the data when it was initially
)