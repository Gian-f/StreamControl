package com.br.streamcontrol.domain.repository

import com.br.streamcontrol.data.local.dao.UserDao
import com.br.streamcontrol.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
) : UserRepository, BaseRepository() {
    override suspend fun insertUser(user: User): Result<Unit> {
        return safeDatabaseCall {
            dao.insertUser(user)
        }
    }

    override suspend fun getUser(): Flow<List<User>> {
        return safeDatabaseCall {
            dao.getAllUser()
        }.getOrElse {
            emptyFlow()
        }
    }

    override suspend fun deleteAllUser(): Result<Unit> {
        return safeDatabaseCall {
            dao.deleteAllUser()
        }
    }
}