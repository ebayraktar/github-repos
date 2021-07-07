package com.bayraktar.githubrepos.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val favoriteDao: FavoriteDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allFavorites: Flow<List<Favorite>> = favoriteDao.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(favorite: Favorite) {
        favoriteDao.insert(favorite)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(favorite: Favorite) {
        favoriteDao.delete(favorite)
    }


}