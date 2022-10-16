package com.studioviolet.libs.user

import com.studioviolet.libs.storage.UserEntity
import com.studioviolet.libs.storage.WordleDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface UserRepository {
    val currentUsername: Flow<String?>
    suspend fun setCurrentUsername(username: String)
}

private const val USER_ID = "1234"

@Singleton
internal class UserRepositoryImpl @Inject constructor(
    private val database: WordleDatabase
): UserRepository {
    private val _currentUsername = database.userDao().getById(USER_ID).map { it?.name }

    override val currentUsername: Flow<String?>
        get() = _currentUsername

    override suspend fun setCurrentUsername(username: String) {
        database.userDao().update(UserEntity(USER_ID, username))
    }
}
