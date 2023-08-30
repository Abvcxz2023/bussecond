package com.example.bussecond.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bookmark: Bookmark)

    @Update
    suspend fun update(bookmark: Bookmark)

    @Delete
    suspend fun delete(bookmark: Bookmark)

    @Query("SELECT * from bookmarks WHERE route = :route AND bound = :bound AND seq = :seq")
    fun getBookmark(route: String, bound: String, seq: Int): Flow<Bookmark>

    @Query("SELECT * from bookmarks")
    fun getAllBookmark(): Flow<List<Bookmark>>

    @Query("DELETE from bookmarks WHERE route = :route AND bound = :bound AND seq = :seq")
    suspend fun deleteBookmark(route: String, bound: String, seq: Int)
}