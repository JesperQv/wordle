package com.studioviolet.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studioviolet.wordle.ui.game.GuessesComponent
import com.studioviolet.wordle.ui.game.KeyboardComponent
import com.studioviolet.wordle.ui.theme.Typography
import com.studioviolet.wordle.ui.theme.WordleTheme
import com.studioviolet.wordle.viewmodel.GameState
import com.studioviolet.wordle.viewmodel.ResultState
import com.studioviolet.wordle.viewmodel.WordleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WordleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getNewWord()
        setContent {
            WordleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val gameState = viewModel.gameState.value
                    val resultState = viewModel.resultState.value
                    val correctWord = viewModel.correctWord
                    val alphabetState = viewModel.alphabetState
                    val guessValidityState = viewModel.guessValidityState.value
                    when (gameState) {
                        is GameState.Loading -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(bottom = 12.dp),
                                    text = "Wordle",
                                    style = Typography.h1,
                                    textAlign = TextAlign.Center
                                )
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .width(50.dp)
                                        .height(50.dp)
                                )
                            }
                        }
                        is GameState.Playing -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier.weight(0.05f),
                                    text = "Wordle",
                                    style = Typography.h1,
                                    textAlign = TextAlign.Center
                                )
                                GuessesComponent(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(0.7f),
                                    showGuessRow = resultState == ResultState.IDLE,
                                    previousGuesses = gameState.previousGuesses,
                                    currentGuess = gameState.currentGuess,
                                    onLetterClick = { index ->
                                        viewModel.dropCharactersFromGuess(index)
                                    }
                                )
                                if (resultState == ResultState.WIN || resultState == ResultState.LOSE) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().weight(0.3f),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            modifier = Modifier
                                                .padding(bottom = 12.dp),
                                            text = "The word was $correctWord",
                                            style = Typography.h2,
                                            textAlign = TextAlign.Center
                                        )
                                        Button(modifier = Modifier
                                            .width(150.dp)
                                            .height(40.dp),
                                            onClick = { viewModel.resetGame() }) {
                                            Text(text = "Play again")
                                        }
                                    }
                                } else {
                                    KeyboardComponent(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(0.3f),
                                        alphabetState = alphabetState,
                                        guessValidityState = guessValidityState,
                                        onLetterClicked = { character ->
                                            viewModel.addCharacterToGuess(character)
                                        },
                                        onGuessButtonClicked = {
                                            viewModel.makeGuess()
                                        },
                                        onDeleteButtonClicked = {
                                            viewModel.dropLastCharacterFromGuess()
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
