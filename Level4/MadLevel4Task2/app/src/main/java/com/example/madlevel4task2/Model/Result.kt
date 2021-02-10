package com.example.madlevel4task2.Model

enum class Result{
    COMPUTER_WINS,
    DRAW,
    PLAYER_WINS
}

/**
 * To the total of wins, draws and losses
 */
class ResultOverView(val totalWins:Int, val totalDraws:Int, val totalLoss:Int)