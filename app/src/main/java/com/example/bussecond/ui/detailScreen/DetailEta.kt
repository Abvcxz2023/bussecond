package com.example.bussecond.ui.detailScreen


data class DetailEta(
    val route: String = "", //The bus route number of the requested bus company
    val dir: String = "",//The direction of the bus route
    val seq: Int = 0, //The stop sequence number of a bus route
    val destinationTc: String = "",//The destination of a bus route in Traditional Chinese
    val destinationSc: String = "",//The destination of a bus route in Simplified Chinese.
    val destinationEn: String = "",//The destination of a bus route in English
    val etaSequence: Int = 0,//The sequence number of ETA
    val eta: List<String> = listOf(),//The timestamp of the next ETA
    val remarkTc: String = "",//The remark of an ETA in Traditional Chinese
    val remarkSc: String = "",//The remark of an ETA in Simplified Chinese.
    val remarkEn: String = "",//The remark of an ETA in English
    val stopName: String = "",
)
