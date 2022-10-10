package com.studioviolet.wordle.ui.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studioviolet.wordle.viewmodel.GuessValidityState
import com.studioviolet.wordle.viewmodel.Letter

@Composable
fun KeyboardComponent(
    modifier: Modifier = Modifier,
    alphabetState: Map<Char, Letter>,
    onLetterClicked: (Char) -> Unit,
    guessValidityState: GuessValidityState,
    onGuessButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            KeyComponent(letter = alphabetState['Q']) { onLetterClicked.invoke('Q') }
            KeyComponent(letter = alphabetState['W']) { onLetterClicked.invoke('W') }
            KeyComponent(letter = alphabetState['E']) { onLetterClicked.invoke('E') }
            KeyComponent(letter = alphabetState['R']) { onLetterClicked.invoke('R') }
            KeyComponent(letter = alphabetState['T']) { onLetterClicked.invoke('T') }
            KeyComponent(letter = alphabetState['Y']) { onLetterClicked.invoke('Y') }
            KeyComponent(letter = alphabetState['U']) { onLetterClicked.invoke('U') }
            KeyComponent(letter = alphabetState['I']) { onLetterClicked.invoke('I') }
            KeyComponent(letter = alphabetState['O']) { onLetterClicked.invoke('O') }
            KeyComponent(letter = alphabetState['P']) { onLetterClicked.invoke('P') }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            KeyComponent(letter = alphabetState['A']) { onLetterClicked.invoke('A') }
            KeyComponent(letter = alphabetState['S']) { onLetterClicked.invoke('S') }
            KeyComponent(letter = alphabetState['D']) { onLetterClicked.invoke('D') }
            KeyComponent(letter = alphabetState['F']) { onLetterClicked.invoke('F') }
            KeyComponent(letter = alphabetState['G']) { onLetterClicked.invoke('G') }
            KeyComponent(letter = alphabetState['H']) { onLetterClicked.invoke('H') }
            KeyComponent(letter = alphabetState['J']) { onLetterClicked.invoke('J') }
            KeyComponent(letter = alphabetState['K']) { onLetterClicked.invoke('K') }
            KeyComponent(letter = alphabetState['L']) { onLetterClicked.invoke('L') }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            KeyComponent(letter = alphabetState['Z']) { onLetterClicked.invoke('Z') }
            KeyComponent(letter = alphabetState['X']) { onLetterClicked.invoke('X') }
            KeyComponent(letter = alphabetState['C']) { onLetterClicked.invoke('C') }
            KeyComponent(letter = alphabetState['V']) { onLetterClicked.invoke('V') }
            KeyComponent(letter = alphabetState['B']) { onLetterClicked.invoke('B') }
            KeyComponent(letter = alphabetState['N']) { onLetterClicked.invoke('N') }
            KeyComponent(letter = alphabetState['M']) { onLetterClicked.invoke('M') }
            DeleteKeyComponent { onDeleteButtonClicked.invoke() }
        }
        Button(
            modifier = Modifier
                .width(150.dp)
                .height(40.dp),
            enabled = guessValidityState == GuessValidityState.Valid,
            onClick = {
                onGuessButtonClicked.invoke()
            }
        ) {
            when (guessValidityState) {
                is GuessValidityState.Valid -> {
                    Text(text = "Guess")
                }
                is GuessValidityState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier
                        .width(30.dp)
                        .height(30.dp))
                }
                else -> {
                    Text(text = "Not a word")
                }
            }
        }
    }
}

@Composable
fun DeleteKeyComponent(
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    Card(
        modifier = Modifier
            .width(50.dp)
            .height(30.dp)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick.invoke()
            },
        backgroundColor = Color.LightGray,
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "<",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun KeyComponent(
    letter: Letter?,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val character = letter?.character ?: ""
    val text = character.toString()
    val backgroundColor = when (letter) {
        is Letter.RightPosition -> Color.Green
        is Letter.WrongPosition -> Color.Yellow
        is Letter.NotInWord -> Color.DarkGray
        is Letter.Unknown, null -> Color.LightGray
    }
    Card(
        modifier = Modifier
            .width(30.dp)
            .height(30.dp)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick.invoke()
            },
        backgroundColor = backgroundColor,
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}
