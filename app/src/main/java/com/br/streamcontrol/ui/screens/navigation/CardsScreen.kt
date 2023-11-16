package com.br.streamcontrol.ui.screens.navigation

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.br.streamcontrol.domain.enums.CardType
import com.br.streamcontrol.domain.routes.OnBackPress
import com.br.streamcontrol.domain.routes.Router
import com.br.streamcontrol.domain.routes.Screen
import com.br.streamcontrol.domain.viewmodel.CardViewModel
import com.br.streamcontrol.util.CardNumberFilter
import com.br.streamcontrol.util.MaskTransformation
import com.br.streamcontrol.util.hideCreditCardNumber

@Composable
fun CardsContent(
    contentPadding: PaddingValues,
) {
    val cardViewModel: CardViewModel = viewModel()
    val cards by cardViewModel.cards.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (cards.isEmpty()) {
            item {
                Text(
                    text = "Nenhum Cartão Cadastrado!"
                )
            }
        } else {
            items(cards) { card ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(horizontal = 16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier.padding(start = 12.dp),
                            style = MaterialTheme.typography.titleLarge,
                            text = card.flag
                        )
                        Text(
                            text = hideCreditCardNumber(card.cardNumber),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp),
                        style = MaterialTheme.typography.titleLarge,
                        text = card.holderName
                    )
                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(bottom = 8.dp)
                    ) {
                        Text(
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            text = card.dueDate.take(4).chunked(2).joinToString(" / ")
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}


@Composable
fun CardDetails() {
    val viewModel: CardViewModel = viewModel()
    val handler = Handler(Looper.getMainLooper())
    val context = LocalContext.current
    var isSaving by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        CreditCard(
            cardNumber = viewModel.cardNumber,
            holderName = viewModel.cardHolderName,
            expiryDate = viewModel.expiryDate,
            cardCVV = viewModel.cardCVV,
            viewModel = viewModel
        )

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            item {
                OutlinedTextField(
                    value = viewModel.cardNumber.text,
                    onValueChange = { viewModel.cardNumber = TextFieldValue(it) },
                    label = { Text("Seu número de cartão") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    visualTransformation = CardNumberFilter
                )
            }

            item {
                OutlinedTextField(
                    value = viewModel.cardHolderName.text,
                    onValueChange = { viewModel.cardHolderName = TextFieldValue(it) },
                    label = { Text("Seu nome no cartão") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = viewModel.expiryDate.text,
                        onValueChange = { viewModel.expiryDate = TextFieldValue(it) },
                        label = { Text("Validade") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        visualTransformation = MaskTransformation(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = viewModel.cardCVV.text,
                        onValueChange = { viewModel.cardCVV = TextFieldValue(it) },
                        label = { Text("CVV") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        isSaving = true
                        handler.postDelayed({
                            isSaving = false
                            viewModel.saveLocalCard()
                            Router.navigateTo(Screen.HomeScreen)
                            Toast.makeText(
                                context,
                                "Operação realizada com sucesso!",
                                Toast.LENGTH_LONG
                            ).show()
                        }, 1000)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    shape = RoundedCornerShape(15.dp),
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(
                            text = "Salvar",
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 30.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }

    OnBackPress {
        Router.navigateTo(Screen.HomeScreen)
    }
}

@Composable
fun CreditCard(
    cardNumber: TextFieldValue,
    holderName: TextFieldValue,
    expiryDate: TextFieldValue,
    cardCVV: TextFieldValue,
    viewModel: CardViewModel,
) {

    // Mutable state to track the flip state of the card
    var backSwitch by remember { mutableStateOf(false) }

    // Mutable state to track the detected card type (Visa, Mastercard, etc.)
    var cardType by remember { mutableStateOf(CardType.None) }

    // Calculate the length of the card number and mask it for display
    val length = if (cardNumber.text.length > 16) 16 else cardNumber.text.length
    val maskedNumber =
        remember { "*****************" }.replaceRange(0..length, cardNumber.text.take(16))


    val cvv = if (cardCVV.text.length > 3) 3 else cardCVV.text.length
    val maskedCVV = remember { "*".repeat(3) }.replaceRange(0 until cvv, cardCVV.text.take(3))

    // Determine whether to switch to the back side of the card based on CVV length
    if (cardCVV.text.length == 1 && !backSwitch) {
        backSwitch = true
    } else if (cardCVV.text.length == 2) {
        backSwitch = true
    } else if (cardCVV.text.length == 3) {
        backSwitch = false
    }

    // Detect and set the card type logo based on the card number's first digit
    cardType = when {
        cardNumber.text.isNotEmpty() -> {
            when (cardNumber.text.take(2)) {
                "30", "36", "38" -> {
                    CardType.DinersClub
                }

                "40" -> {
                    CardType.Visa
                }

                "50", "51", "52", "53", "54", "55" -> {
                    CardType.Mastercard
                }

                "56", "57", "58", "63", "67" -> {
                    CardType.Maestro
                }

                "60" -> {
                    CardType.RuPay
                }

                "37" -> {
                    CardType.AmericanExpress
                }

                else -> {
                    CardType.None
                }
            }
        }

        else -> CardType.None
    }

    viewModel.flag = cardType.title

    // Set the card's background color based on its type
    val animatedColor = animateColorAsState(
        targetValue =
        when (cardType) {
            CardType.Visa -> {
                Color(0xFF1C478B)
            }

            CardType.Mastercard -> {
                Color(0xFF3BB9A1)
            }

            CardType.RuPay -> {
                Color(0xFFB2B1FD)
            }

            CardType.AmericanExpress -> {
                Color(0xFFA671FC)
            }

            CardType.Maestro -> {
                Color(0xFF99BEF8)
            }

            CardType.DinersClub -> {
                Color(0xFFFC4444)
            }

            else -> {
                Color(0xFF1C478B)
            }
        },
        label = ""
    )

    Box {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(200.dp)
                .graphicsLayer(
                    rotationY = animateFloatAsState(
                        if (backSwitch) 180f else 0f,
                        label = "",
                        animationSpec = tween(500),
                    ).value,
                    translationY = 0f
                )
                .clip(RoundedCornerShape(8))
                .clickable {
                    backSwitch = !backSwitch
                },
            shape = RoundedCornerShape(20.dp),
            color = animatedColor.value,
            shadowElevation = 18.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                AnimatedVisibility(visible = !backSwitch) {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        val (cardImage, cardName, cardHolderName, number, cardExpiry, expiry) = createRefs()

                        AnimatedVisibility(visible = cardType != CardType.None,
                            modifier = Modifier
                                .padding(start = 12.dp, top = 10.dp)
                                .constrainAs(cardImage) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                }) {
                            Image(
                                painter = painterResource(id = cardType.image),
                                contentDescription = "Card Image"
                            )
                        }

                        Text(
                            text = maskedNumber.chunked(4).joinToString(" "),
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1,
                            color = Color.White,
                            modifier = Modifier
                                .animateContentSize(spring())
                                .padding(bottom = 20.dp)
                                .constrainAs(number) {
                                    linkTo(
                                        start = parent.start,
                                        end = parent.end
                                    )
                                    linkTo(
                                        top = parent.top,
                                        bottom = parent.bottom
                                    )
                                }
                        )

                        Text(
                            text = "Nome do Titular",
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .constrainAs(cardHolderName) {
                                    start.linkTo(parent.start)
                                    bottom.linkTo(cardName.top)
                                }
                        )

                        Text(
                            text = holderName.text,
                            color = Color.White,
                            modifier = Modifier
                                .animateContentSize(TweenSpec(300))
                                .padding(top = 10.dp, start = 16.dp, bottom = 16.dp)
                                .constrainAs(cardName) {
                                    start.linkTo(parent.start)
                                    bottom.linkTo(parent.bottom)
                                }
                        )

                        Text(
                            text = "Validade",
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .constrainAs(expiry) {
                                    end.linkTo(parent.end)
                                    bottom.linkTo(cardExpiry.top)
                                }
                        )

                        Text(
                            text = expiryDate.text.take(4).chunked(2).joinToString(" / "),
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 10.dp, end = 16.dp, bottom = 16.dp)
                                .constrainAs(cardExpiry) {
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }
                        )
                    }
                }

                AnimatedVisibility(visible = backSwitch) {
                    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                        val (back) = createRefs()
                        Spacer(modifier = Modifier
                            .height(50.dp)
                            .background(
                                Color.Black
                            )
                            .fillMaxWidth()
                            .constrainAs(back) {
                                linkTo(
                                    top = parent.top,
                                    bottom = parent.bottom
                                )
                            }
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = backSwitch,
            modifier = Modifier
                .padding(end = 50.dp, bottom = 50.dp)
                .align(Alignment.BottomEnd)
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = 60.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = maskedCVV,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    modifier = Modifier
                        .animateContentSize(TweenSpec(300))
                        .padding(vertical = 4.dp, horizontal = 16.dp)

                )
            }
        }
    }
}

