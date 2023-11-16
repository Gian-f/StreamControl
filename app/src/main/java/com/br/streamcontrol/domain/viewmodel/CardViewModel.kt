package com.br.streamcontrol.domain.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.streamcontrol.data.model.CardPayment
import com.br.streamcontrol.domain.repository.CardRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val repository: CardRepositoryImpl,
) : ViewModel() {
    var cardNumber by mutableStateOf(TextFieldValue())
    var cardHolderName by mutableStateOf(TextFieldValue())
    var expiryDate by mutableStateOf(TextFieldValue())
    var cardCVV by mutableStateOf(TextFieldValue())
    var flag = ""

    private val _cards = MutableStateFlow<List<CardPayment>>(emptyList())
    val cards: StateFlow<List<CardPayment>> = _cards

    private val TAG = CardViewModel::class.simpleName

    init {
        getAllCards()
    }

    fun saveLocalCard() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "$throwable")
        }) {
            val card = CardPayment(
                cardNumber = cardNumber.text,
                holderName = cardHolderName.text,
                flag = flag,
                dueDate = expiryDate.text,
                cvv = cardCVV.text,
            )
            repository.insertCard(card)
            clearForm()
            getAllCards()
        }
    }

    fun getAllCards() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "$throwable")
        }) {
            repository.getCards().collect(_cards)
        }
    }

    fun clearForm() {
        cardNumber = TextFieldValue()
        cardHolderName = TextFieldValue()
        expiryDate = TextFieldValue()
        cardCVV = TextFieldValue()
        flag = ""
    }
}