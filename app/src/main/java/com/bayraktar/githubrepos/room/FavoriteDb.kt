package com.bayraktar.githubrepos.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Favorite::class], version = 1)
abstract class FavoriteRoomDb : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRoomDb? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): FavoriteRoomDb {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteRoomDb::class.java,
                    "favorite_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(FavoriteDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class FavoriteDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
//                INSTANCE?.let { database ->
//                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.favoriteDao())
//                    }
//                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more favorites, just add them.
         */
        suspend fun populateDatabase(favoriteDao: FavoriteDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            favoriteDao.deleteAll()

//            var favorite = Favorite("Hello")
//            favoriteDao.insert(favorite)
//            favorite = Favorite("World!")
//            favoriteDao.insert(favorite)
        }
    }
}
