package com.studioviolet.libs.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "first_name") val name: String
)

@Entity(tableName = "gameStat")
data class GameStatEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "win") val win: Boolean,
    @ColumnInfo(name = "numberOfGuesses") val numberOfGuesses: Int,
    @ColumnInfo(name = "firstWord") val firstGuess: String,
    @ColumnInfo(name = "secondWord") val secondWord: String,
)
