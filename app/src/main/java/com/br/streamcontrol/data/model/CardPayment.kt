package com.br.streamcontrol.data.model

data class CardPayment(
    val cardNumber: String,
    val holderName: String,
    val dueDate: String,
    val flag: String
)
