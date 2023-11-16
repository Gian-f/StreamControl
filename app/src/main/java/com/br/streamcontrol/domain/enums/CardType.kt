package com.br.streamcontrol.domain.enums

import androidx.annotation.DrawableRes
import com.br.streamcontrol.R

enum class CardType(
    val title: String,
    @DrawableRes val image: Int,
) {
    None("Nada", R.drawable.visa),
    Visa("Visa", R.drawable.visa),
    Mastercard("Mastercard", R.drawable.mastercard),
    RuPay("Rupay", R.drawable.rupay_logo),
    AmericanExpress("American Express", R.drawable.amex_logo),
    Maestro("Maestro", R.drawable.maestro),
    DinersClub("DinersClub", R.drawable.diners_club)
}