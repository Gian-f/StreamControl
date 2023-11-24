package com.br.streamcontrol.domain.repository

import com.br.streamcontrol.data.local.dao.CardDao
import com.br.streamcontrol.data.model.CardPayment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val dao: CardDao,
) : CardRepository, BaseRepository() {
    override suspend fun insertCard(card: CardPayment): Result<Unit> {
        return safeDatabaseCall {
            dao.insertCard(card)
        }
    }

    override suspend fun getCards(): Flow<List<CardPayment>> {
        return safeDatabaseCall {
            dao.getAllCard()
        }.getOrElse {
            emptyFlow()
        }
    }
}