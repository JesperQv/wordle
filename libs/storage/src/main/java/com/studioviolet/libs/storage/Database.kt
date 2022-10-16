package com.studioviolet.libs.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameStatEntity::class, UserEntity::class], version = 1)
abstract class WordleDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gameStatDao(): GameStatDao
}
