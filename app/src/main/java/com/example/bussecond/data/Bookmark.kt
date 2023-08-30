package com.example.bussecond.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks", primaryKeys = ["route","bound","seq"])
data class Bookmark(
    val route: String,
    val bound: String,
    val seq: Int,
    val destination: String,
    val stopname: String,
    val stopId: String
)