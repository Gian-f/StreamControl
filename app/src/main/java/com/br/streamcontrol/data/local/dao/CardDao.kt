package com.br.streamcontrol.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.br.streamcontrol.data.model.CardPayment
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert
    suspend fun insertCard(card: CardPayment)

    @Update
    suspend fun updateCard(card: CardPayment)
    @Query("DELETE FROM cardpayment")
    suspend fun deleteAllCard()

    @Query("SELECT * FROM cardpayment WHERE id = :id")
    suspend fun getCardById(id: Int): CardPayment?

    @Query("SELECT * FROM CardPayment")
    fun getAllCard(): Flow<List<CardPayment>>
}