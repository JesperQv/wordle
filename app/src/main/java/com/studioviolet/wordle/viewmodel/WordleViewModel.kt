package com.studioviolet.wordle.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studioviolet.wordle.repository.WordleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class GuessValidityState {
    object Valid : GuessValidityState()
    object Invalid : GuessValidityState()
    object Loading : GuessValidityState()
    class Error(val message: String) : GuessValidityState()
}

sealed class GameState {
    object Loading : GameState()
    class Playing(val previousGuesses: List<Guess>, val currentGuess: Guess) : GameState()
}

enum class ResultState {
    IDLE, WIN, LOSE
}

data class Guess(val letters: List<Letter?>)

sealed class Letter(val character: Char) {
    class Unknown(character: Char) : Letter(character)
    class NotInWord(character: Char) : Letter(character)
    class WrongPosition(character: Char) : Letter(character)
    class RightPosition(character: Char) : Letter(character)
}

const val WORD_SIZE = 5
private const val MAX_GUESSES = 6

@HiltViewModel
class WordleViewModel @Inject constructor(
    private val repository: WordleRepository
) : ViewModel() {

    private var correctWord: String? = null
    private var guesses = mutableListOf<Guess>()
    private var currentGuess: String = ""

    var guessValidityState = mutableStateOf<GuessValidityState>(GuessValidityState.Invalid)
    var gameState = mutableStateOf<GameState>(GameState.Loading)
    var resultState = mutableStateOf(ResultState.IDLE)
    var alphabetState = defaultAlphabetState

    fun addCharacterToGuess(character: Char) {
        if (currentGuess.length == WORD_SIZE) return
        currentGuess += character
        gameState.value = GameState.Playing(guesses, currentGuess.toUnknownGuess())
        if (currentGuess.length == WORD_SIZE) {
            validateGuess()
        } else {
            guessValidityState.value = GuessValidityState.Invalid
        }
    }

    fun dropCharactersFromGuess(fromIndex: Int) {
        while (currentGuess.length > fromIndex) {
            currentGuess = currentGuess.dropLast(1)
        }
        gameState.value = GameState.Playing(guesses, currentGuess.toUnknownGuess())
    }

    fun dropLastCharacterFromGuess() {
        if (currentGuess.isNotEmpty()) {
            currentGuess = currentGuess.dropLast(1)
        }
        gameState.value = GameState.Playing(guesses, currentGuess.toUnknownGuess())
    }

    fun makeGuess() {
        val correctWord = correctWord
        if (correctWord == null || currentGuess.length != WORD_SIZE) return
        if (currentGuess == correctWord) {
            guesses.add(currentGuess.toGuess(correctWord))
            resultState.value = ResultState.WIN
            updateAlphabetState()
            return
        }

        val guess = currentGuess.toGuess(correctWord)
        guesses.add(guess)
        updateAlphabetState()

        if (guesses.size >= MAX_GUESSES) {
            resultState.value = ResultState.LOSE
        } else {
            currentGuess = ""
            gameState.value = GameState.Playing(guesses, currentGuess.toUnknownGuess())
        }
    }

    private fun updateAlphabetState() {
        val word = correctWord ?: return
        val guess = currentGuess.toGuess(word)
        guess.letters.forEach { letter ->
            if (letter != null) {
                val char = letter.character
                when (letter) {
                    is Letter.RightPosition -> {
                        alphabetState[char] = Letter.RightPosition(char)
                    }
                    is Letter.WrongPosition -> {
                        if (alphabetState[char] !is Letter.RightPosition) {
                            alphabetState[char] = Letter.WrongPosition(char)
                        }
                    }
                    is Letter.NotInWord -> {
                        alphabetState[char] = Letter.NotInWord(char)
                    }
                    else -> {
                        // Do nothing
                    }
                }
            }
        }
    }

    fun resetGame() {
        resultState.value = ResultState.IDLE
        guesses.clear()
        currentGuess = ""
        clearAlphabet()
        getNewWord()
    }

    private fun clearAlphabet() {
        alphabetState.forEach {
            alphabetState[it.key] = Letter.Unknown(it.value.character)
        }
    }

    fun getNewWord() {
        gameState.value = GameState.Loading
        viewModelScope.launch {
            try {
                val word = repository.getRandomWord()
                correctWord = word
                gameState.value = GameState.Playing(guesses, currentGuess.toUnknownGuess())
            } catch (e: Exception) {
                //gameState.value = GameState.Error(e.message.orEmpty())
            }
        }
    }

    private fun validateGuess() {
        guessValidityState.value = GuessValidityState.Loading
        viewModelScope.launch {
            try {
                val valid = repository.isValid(currentGuess)
                if (valid) {
                    guessValidityState.value = GuessValidityState.Valid
                } else {
                    guessValidityState.value = GuessValidityState.Invalid
                }
            } catch (e: Exception) {
                guessValidityState.value = GuessValidityState.Error(e.message.orEmpty())
            }
        }
    }
}

private fun String.toGuess(correctWord: String): Guess {
    val letters = mutableListOf<Letter?>()
    forEachIndexed { index, character ->
        if (index >= WORD_SIZE) {
            // Nothing
        } else if (!correctWord.contains(character)) {
            letters.add(index, Letter.NotInWord(character))
        } else if (correctWord[index] == character) {
            letters.add(index, Letter.RightPosition(character))
        } else {
            letters.add(index, Letter.WrongPosition(character))
        }
    }

    while (letters.size < WORD_SIZE) {
        letters.add(null)
    }

    return Guess(letters)
}

private val defaultAlphabetState = mutableStateMapOf<Char, Letter>(
    Pair('A', Letter.Unknown('A')),
    Pair('B', Letter.Unknown('B')),
    Pair('C', Letter.Unknown('C')),
    Pair('D', Letter.Unknown('D')),
    Pair('E', Letter.Unknown('E')),
    Pair('F', Letter.Unknown('F')),
    Pair('G', Letter.Unknown('G')),
    Pair('H', Letter.Unknown('H')),
    Pair('I', Letter.Unknown('I')),
    Pair('J', Letter.Unknown('J')),
    Pair('K', Letter.Unknown('K')),
    Pair('L', Letter.Unknown('L')),
    Pair('M', Letter.Unknown('M')),
    Pair('N', Letter.Unknown('N')),
    Pair('O', Letter.Unknown('O')),
    Pair('P', Letter.Unknown('P')),
    Pair('Q', Letter.Unknown('Q')),
    Pair('R', Letter.Unknown('R')),
    Pair('S', Letter.Unknown('S')),
    Pair('T', Letter.Unknown('T')),
    Pair('U', Letter.Unknown('U')),
    Pair('V', Letter.Unknown('V')),
    Pair('W', Letter.Unknown('W')),
    Pair('X', Letter.Unknown('X')),
    Pair('Y', Letter.Unknown('Y')),
    Pair('Z', Letter.Unknown('Z')),
)

private fun String.toUnknownGuess(): Guess {
    val letters = mutableListOf<Letter?>()
    forEach {
        letters.add(Letter.Unknown(it))
    }
    while(letters.size < WORD_SIZE) {
        letters.add(null)
    }
    return Guess(letters)
}
