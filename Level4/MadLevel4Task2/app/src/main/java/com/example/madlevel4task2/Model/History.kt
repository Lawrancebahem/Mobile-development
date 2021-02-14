package com.example.madlevel4task2.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.madlevel4task2.R
import java.util.*


@Entity(tableName = "History_table")
class History(
    @ColumnInfo
    val RPSComputer: RPS,
    @ColumnInfo
    val RPSPlayer: RPS,
    @ColumnInfo
    val date: Date,
    @ColumnInfo
    val result:Result,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Long? = null,
) {

    companion object {
        const val WINNER = "You win"

        /**
         * This is static method to determine the result
         */

        fun getWinner(computer: RPS, player: RPS): Result {
            when (computer) {
                RPS.SCISSORS -> return when (player) {
                    RPS.SCISSORS -> Result.DRAW
                    RPS.ROCK -> Result.PLAYER_WINS
                    else -> Result.COMPUTER_WINS
                }
                RPS.ROCK -> return when (player) {
                    RPS.SCISSORS -> Result.COMPUTER_WINS
                    RPS.ROCK -> Result.DRAW
                    else -> Result.PLAYER_WINS
                }
                //computer has RSP paper
                else -> return when (player) {
                    RPS.SCISSORS -> Result.PLAYER_WINS
                    RPS.ROCK -> Result.COMPUTER_WINS
                    else -> Result.DRAW
                }
            }
        }


        /**
         * This methos is to get the image based on the given RPS (rock, paper or scissors)
         */
        fun getImageBasedOnRPS(rps: RPS): Int {
            return when (rps) {
                RPS.ROCK -> R.drawable.rock
                RPS.SCISSORS -> R.drawable.scissors
                else -> R.drawable.paper
            }
        }


        /**
         *
         */
        fun determineResultAsText(result: Result): String {
            return when (result) {
                Result.PLAYER_WINS -> WINNER
                Result.DRAW -> "Draw"
                else -> "Computer wins!"
            }
        }
    }

}