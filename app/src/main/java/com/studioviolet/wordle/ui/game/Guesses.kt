package com.studioviolet.wordle.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.studioviolet.wordle.viewmodel.Guess

@Composable
fun GuessesComponent(
    modifier: Modifier = Modifier,
    previousGuesses: List<Guess>,
    showGuessRow: Boolean,
    currentGuess: Guess,
    onLetterClick: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        previousGuesses.forEach { guess ->
            GuessRowComponent(
                modifier = Modifier.padding(16.dp),
                guess = guess,
                clickableLetters = false,
                onLetterClick = onLetterClick
            )
        }
        if (showGuessRow) {
            GuessRowComponent(
                modifier = Modifier.padding(16.dp),
                guess = currentGuess,
                clickableLetters = true,
                onLetterClick = onLetterClick
            )
        }
    }
}
