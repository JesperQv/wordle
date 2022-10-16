package com.studioviolet.features.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.studioviolet.libs.statistics.StatisticsRepository
import com.studioviolet.libs.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class Profile(
    val username: String? = null,
    val winRate: Double? = null,
    val bestFirstGuess: BestFirstGuess? = null
)

data class BestFirstGuess(
    private val word: String? = null,
    private val averageGuesses: Double? = null
)

sealed class ProfileState {
    object Loading : ProfileState()
    class Loaded(val profile: Profile) : ProfileState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var profileState = mutableStateOf<ProfileState>(ProfileState.Loading)

    private var currentProfile: Profile? = null

    init {
        viewModelScope.launch {
            userRepository.currentUsername.collect { currentUsername ->
                val profile = currentProfile
                currentProfile =
                    profile?.copy(username = currentUsername) ?: Profile(username = currentUsername)

                currentProfile?.let { profileState.value = ProfileState.Loaded(it) }
            }

            statisticsRepository.winRate.collect { currentWinRate ->
                val profile = currentProfile
                currentProfile =
                    profile?.copy(winRate = currentWinRate) ?: Profile(winRate = currentWinRate)
                currentProfile?.let { profileState.value = ProfileState.Loaded(it) }
            }

            statisticsRepository.bestFirstGuess.collect { currentBestFirstGuess ->
                val profile = currentProfile

                val averageGuesses =
                    statisticsRepository.getAverageGuessesByFirstWord(currentBestFirstGuess)

                val newBestFirstGuess = BestFirstGuess(
                    word = currentBestFirstGuess,
                    averageGuesses = averageGuesses
                )

                currentProfile =
                    profile?.copy(bestFirstGuess = newBestFirstGuess)
                        ?: Profile(bestFirstGuess = newBestFirstGuess)
                currentProfile?.let { profileState.value = ProfileState.Loaded(it) }
            }
        }
    }
}
