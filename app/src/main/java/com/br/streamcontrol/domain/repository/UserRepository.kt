package com.br.streamcontrol.domain.repository

import com.br.streamcontrol.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun insertUser(user: User): Result<Unit>

    suspend fun getUser(): Flow<List<User>>

    suspend fun deleteAllUser(): Result<Unit>
}