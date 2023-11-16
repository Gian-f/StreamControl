package com.br.streamcontrol.domain.repository

import com.br.streamcontrol.data.local.dao.CardDao
import com.br.streamcontrol.data.model.CardPayment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val dao: CardDao,
) : CardRepository {
    override suspend fun insertCard(card: CardPayment) {
        return dao.insertCard(card)
    }

    override suspend fun getCards(): Flow<List<CardPayment>> {
        return dao.getAllCard()
    }
}