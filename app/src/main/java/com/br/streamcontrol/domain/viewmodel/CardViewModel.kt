package com.br.streamcontrol.domain.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br.streamcontrol.data.model.CardPayment
import com.br.streamcontrol.domain.repository.CardRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val repository: CardRepositoryImpl,
) : ViewModel() {
    private val TAG = CardViewModel::class.simpleName

    private val card = MutableLiveData<CardPayment>()


    fun saveLocalCard() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "$throwable")
        }) {
            val card = CardPayment(
                cardNumber = card.value?.cardNumber ?: "",
                holderName = card.value?.holderName ?: "",
                flag = card.value?.flag ?: "",
                dueDate = card.value?.dueDate ?: "",
            )
            repository.insertCard(card)
        }
    }

    fun getAllCards() {
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "$throwable")
        }) {
            repository.getCards()
            withContext(Dispatchers.Main) {
            }
        }
    }
}