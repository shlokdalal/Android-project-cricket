package com.project.cricketscorer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), RadioGroup.OnCheckedChangeListener {

    private lateinit var startMatch: Button
    private lateinit var teamName1: EditText
    private lateinit var teamName2: EditText
    private lateinit var overs: EditText
    private lateinit var rgOptedFor: RadioGroup
    private lateinit var rgTossWonBy: RadioGroup

    private var tossWinningTeam: String = ""
    private var battingTeamName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startMatch = findViewById(R.id.startMatch)
        teamName1 = findViewById(R.id.team_1)
        teamName2 = findViewById(R.id.team_2)
        overs = findViewById(R.id.overs)
        rgOptedFor = findViewById(R.id.rg_opted_for)
        rgTossWonBy = findViewById(R.id.rg_toss_won_by)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rgOptedFor.setOnCheckedChangeListener(this)
        rgTossWonBy.setOnCheckedChangeListener(this)

        startMatch.setOnClickListener {
            val homeTeamName = teamName1.text.toString().trim()
            val awayTeamName = teamName2.text.toString().trim()
            val oversValue = overs.text.toString().trim()

            if (homeTeamName.isEmpty()) {
                Toast.makeText(this, "Please enter the home team name", Toast.LENGTH_SHORT).show()
            } else if (awayTeamName.isEmpty()) {
                Toast.makeText(this, "Please enter the away team name", Toast.LENGTH_SHORT).show()
            } else if (oversValue.isEmpty()) {
                Toast.makeText(this, "Please enter the number of overs", Toast.LENGTH_SHORT).show()
            } else {

                val intent = Intent(this, ScoreSheet::class.java).apply {
                    putExtra("BATTING_TEAM", battingTeamName)
                    putExtra("HOME_TEAM", homeTeamName)
                    putExtra("AWAY_TEAM", awayTeamName)
                    putExtra("OVERS", oversValue)
                }
                Toast.makeText(this, "Match Started", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rb_home_team -> {
                tossWinningTeam = teamName1.text.toString().trim()
                Toast.makeText(this, "Home team won the toss", Toast.LENGTH_SHORT).show()
            }

            R.id.rb_away_team -> {
                tossWinningTeam = teamName2.text.toString().trim()
                Toast.makeText(this, "Away team won the toss", Toast.LENGTH_SHORT).show()
            }

            R.id.rb_bat_button -> {
                if (tossWinningTeam.isNotEmpty()) {
                    battingTeamName = tossWinningTeam
                    Toast.makeText(this, "$battingTeamName opted to bat", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please select the toss winning team first", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.rb_bowl_button -> {
                if (tossWinningTeam.isNotEmpty()) {
                    battingTeamName = if (tossWinningTeam == teamName1.text.toString().trim()) {
                        teamName2.text.toString().trim()
                    } else {
                        teamName1.text.toString().trim()
                    }
                    Toast.makeText(this, "$battingTeamName will bat", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please select the toss winning team first", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
