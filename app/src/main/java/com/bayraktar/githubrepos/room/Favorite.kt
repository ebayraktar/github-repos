package com.bayraktar.githubrepos.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite(
    val title: String,
    val username: String,
    @PrimaryKey(autoGenerate = false) val id: Long? = null
)