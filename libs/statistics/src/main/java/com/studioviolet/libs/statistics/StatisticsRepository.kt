package com.studioviolet.libs.statistics

import com.studioviolet.libs.storage.WordleDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface StatisticsRepository {
    val winRate: Flow<Double>
    val bestFirstGuess: Flow<String>
    suspend fun getAverageGuessesByFirstWord(firstWord: String): Double
}

@Singleton
internal class StatisticsRepositoryImpl @Inject constructor(
    private val database: WordleDatabase
): StatisticsRepository {

    override val winRate: Flow<Double>
        get() = database.gameStatDao().getAll().map { allGames ->
            val numberOfGames = allGames.size
            val numberOfWins = allGames.filter { it.win }.size
            numberOfWins.toDouble()/numberOfGames.toDouble()
        }
    override val bestFirstGuess: Flow<String>
        get() = database.gameStatDao().getAll().map { allGames ->
            val firstGuesses = allGames.groupingBy { it.firstGuess }
            firstGuesses.eachCount().toSortedMap().firstKey() //TODO test this
        }

    override suspend fun getAverageGuessesByFirstWord(firstWord: String): Double {
        val allWins = database.gameStatDao().getByFirstWord(firstWord).filter { it.win }
        val numberOfGuesses = allWins.map { it.numberOfGuesses }
        return numberOfGuesses.sumOf { it }.toDouble() / allWins.size.toDouble()
    }
}
