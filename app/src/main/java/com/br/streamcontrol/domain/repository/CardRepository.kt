package com.br.streamcontrol.domain.repository

import com.br.streamcontrol.data.model.CardPayment
import com.br.streamcontrol.data.model.User
import kotlinx.coroutines.flow.Flow

interface CardRepository {
    suspend fun insertCard(card: CardPayment)
    suspend fun getCards(): Flow<List<CardPayment>>
}