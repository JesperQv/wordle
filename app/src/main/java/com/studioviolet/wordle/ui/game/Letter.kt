package com.studioviolet.wordle.ui.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studioviolet.wordle.ui.theme.Typography
import com.studioviolet.wordle.ui.theme.WordleGreen
import com.studioviolet.wordle.ui.theme.WordleYellow
import com.studioviolet.wordle.viewmodel.Letter

@Composable
fun LetterComponent(
    letter: Letter?,
    clickable: Boolean,
    onClick: () -> Unit
) {
    val character = letter?.character ?: ""
    val text = character.toString()

    val color = when (letter) {
            is Letter.RightPosition -> WordleGreen
            is Letter.WrongPosition -> WordleYellow
            is Letter.NotInWord -> Color.DarkGray
            is Letter.Unknown, null -> Color.LightGray }

    var cardModifier = Modifier
        .width(40.dp)
        .height(40.dp)
    if (clickable) {
        cardModifier = cardModifier.clickable { onClick.invoke() }
    }
    Card(
        modifier = cardModifier,
        backgroundColor = color,
        border = BorderStroke(2.dp, Color.White)
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = Typography.body1,
                color = Color.Black
            )
        }
    }
}
