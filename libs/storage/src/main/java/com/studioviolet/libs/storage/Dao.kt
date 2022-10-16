package com.studioviolet.libs.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE id LIKE :id")
    fun getById(id: String): Flow<UserEntity?>

    @Insert
    suspend fun insert(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)
}

@Dao
interface GameStatDao {
    @Query("SELECT * FROM gameStat")
    fun getAll(): Flow<List<GameStatEntity>>

    @Query("SELECT * FROM gameStat WHERE id LIKE :id")
    suspend fun getById(id: String): GameStatEntity

    @Query("SELECT * FROM gameStat WHERE win = 1")
    suspend fun getAllWins(): List<GameStatEntity>

    @Query("SELECT * FROM gameStat WHERE firstWord LIKE :firstWord")
    suspend fun getByFirstWord(firstWord: String): List<GameStatEntity>

    @Insert
    suspend fun insert(user: GameStatEntity)

    @Delete
    suspend fun delete(user: GameStatEntity)

    @Update
    suspend fun update(user: GameStatEntity)
}
