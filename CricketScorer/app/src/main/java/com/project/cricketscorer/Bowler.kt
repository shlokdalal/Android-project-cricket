package com.project.cricketscorer

data class Bowler(
    val name: String,
    var overs: Double,
    var maidens: Int,
    var runsGiven: Int,
    var wickets: Int,
    var economyRate: Double,
    val isBowling: Boolean
)