package com.example.madlevel4task2.Database

import androidx.room.TypeConverter
import com.example.madlevel4task2.Model.RPS
import com.example.madlevel4task2.Model.Result
import java.util.*

class Converter{


    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    /**
     * To convert rock, paper and scissors to number
     */
    @TypeConverter
    fun fromRPSToNumber(rps:RPS):Int{
        return when(rps){
            RPS.ROCK -> 0
            RPS.PAPER -> 1
            else -> 2
        }
    }

    /**
     * To convert numbers to rock, paper or scissors
     */
    @TypeConverter
    fun fromNumberToRPS(number:Int):RPS{
        return when(number){
            0 -> RPS.ROCK
            1 -> RPS.PAPER
            else -> RPS.SCISSORS
        }
    }

    /**
     * To convert rock, paper and scissors to number
     */
    @TypeConverter
    fun fromResultToNumber(result:Result):Int{
        return when(result){
            Result.PLAYER_WINS -> 0
            Result.DRAW -> 1
            else -> 2
        }
    }

    /**
     * To convert numbers to rock, paper or scissors
     */
    @TypeConverter
    fun fromNumberToResult(number:Int):Result{
        return when(number){
            0 -> Result.PLAYER_WINS
            1 ->  Result.DRAW
            else -> Result.COMPUTER_WINS
        }
    }

}