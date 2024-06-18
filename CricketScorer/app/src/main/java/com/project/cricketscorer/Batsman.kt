package com.project.cricketscorer

data class Batsman(
    val name: String,
    var isOnStrike: Boolean = false,
    var runs: Int,
    var balls: Int,
    var fours: Int,
    var sixes: Int,
    var strikeRate: Double,
    var retired: Boolean,
    var legByes: Int = 0,
    var byes: Int = 0
)
