package com.bayraktar.githubrepos.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert
    fun insert(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("delete from favorite")
    fun deleteAll()

    @Query("select * from favorite")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorite")
    fun getAll(): Flow<List<Favorite>>

    @Query("SELECT * FROM favorite where username = :username")
    fun getFavorites(username: String): Flow<List<Favorite>>

}