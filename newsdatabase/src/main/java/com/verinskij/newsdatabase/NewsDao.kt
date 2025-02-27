package com.verinskij.newsdatabase

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface NewsDao  {

    @Insert
    fun insertAll()
}