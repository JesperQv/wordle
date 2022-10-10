package com.studioviolet.wordle.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.studioviolet.wordle.viewmodel.Guess

@Composable
fun GuessRowComponent(
    modifier: Modifier = Modifier,
    guess: Guess,
    clickableLetters: Boolean,
    onLetterClick: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LetterComponent(
            letter = guess.letters.getOrNull(0),
            clickable = clickableLetters,
            onClick = {
                onLetterClick.invoke(0)
            }
        )
        LetterComponent(
            letter = guess.letters.getOrNull(1),
            clickable = clickableLetters,
            onClick = {
                onLetterClick.invoke(1)
            }
        )
        LetterComponent(
            letter = guess.letters.getOrNull(2),
            clickable = clickableLetters,
            onClick = {
                onLetterClick.invoke(2)
            }
        )
        LetterComponent(
            letter = guess.letters.getOrNull(3),
            clickable = clickableLetters,
            onClick = {
                onLetterClick.invoke(3)
            }
        )
        LetterComponent(
            letter = guess.letters.getOrNull(4),
            clickable = clickableLetters,
            onClick = {
                onLetterClick.invoke(4)
            }
        )
    }
}
