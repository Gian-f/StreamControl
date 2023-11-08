package com.br.streamcontrol.domain.repository

import com.br.streamcontrol.data.local.dao.UserDao
import com.br.streamcontrol.data.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao
): UserRepository {
    override suspend fun insertUser(user: User) {
        return dao.insertUser(user)
    }

    override suspend fun getUser(): Flow<List<User>> {
        return dao.getAllUser()
    }

    override suspend fun deleteAllUser() {
        return dao.deleteAllUser()
    }
}