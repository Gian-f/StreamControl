package com.br.streamcontrol.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardPayment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cardNumber: String,
    val holderName: String,
    val dueDate: String,
    val cvv: String,
    val flag: String
)
