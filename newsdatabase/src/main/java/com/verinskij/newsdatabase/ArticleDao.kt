package com.verinskij.newsdatabase

import ArticleDBO
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao  {

   @Query("SELECT * FROM articles")
   fun getAll(): Flow<List<ArticleDBO>>

   @Insert
    suspend fun insert(articles: List<ArticleDBO>)

   @Delete
    suspend fun remove(articles: List<ArticleDBO>)

    @Query("DELETE FROM articles")
    suspend fun clean()
}