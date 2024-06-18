package com.project.cricketscorer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Handler
import android.os.Looper

class ScoreSheet : AppCompatActivity() {

    private lateinit var run1: Button
    private lateinit var run2: Button
    private lateinit var run3: Button
    private lateinit var run4: Button
    private lateinit var run6: Button
    private lateinit var run0: Button
    private lateinit var adapter: RunsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var inningsTextView: TextView
    private lateinit var battingTeamTextView: TextView
    private lateinit var oversTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var wideCheckBox: CheckBox
    private lateinit var noBallCheckBox: CheckBox
    private lateinit var byesCheckBox: CheckBox
    private lateinit var legByesCheckBox: CheckBox
    private lateinit var wicketCheckBox: CheckBox
    private lateinit var retireButton: Button
    private lateinit var swapButton: Button
    private lateinit var undoButton: Button
    private lateinit var batsmanAdapter: BatsmanAdapter
    private lateinit var bowlerAdapter: BowlerAdapter
    private lateinit var batsmanRecyclerView: RecyclerView
    private lateinit var bowlerRecyclerView: RecyclerView

    private var totalRuns: Int = 0
    private var totalWickets: Int = 0
    private var ballsBowled: Int = 0
    private var validBallsCount: Int = 0
    private var maxBalls: Int = 0

    private var batsmen: ArrayList<Batsman> = arrayListOf()
    private var bowler: ArrayList<Bowler> = arrayListOf()
    private val history = arrayListOf<Balls>()

    private var innings: Int = 1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_score_sheet)

        // Initialize the run buttons
        run1 = findViewById(R.id.button1)
        run2 = findViewById(R.id.button2)
        run3 = findViewById(R.id.button3)
        run4 = findViewById(R.id.button4)
        run6 = findViewById(R.id.button6)
        run0 = findViewById(R.id.button0)

        // Initialize the other views
        wideCheckBox = findViewById(R.id.wide)
        noBallCheckBox = findViewById(R.id.no_ball)
        byesCheckBox = findViewById(R.id.byes)
        legByesCheckBox = findViewById(R.id.leg_byes)
        wicketCheckBox = findViewById(R.id.wicket)
        retireButton = findViewById(R.id.retire_button)
        swapButton = findViewById(R.id.swap)
        undoButton = findViewById(R.id.undobutton)

        setRunButtonListeners()
        setExtraListeners()

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.runs_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = RunsAdapter(arrayListOf())
        recyclerView.adapter = adapter

        val overs = intent.getStringExtra("OVERS")?.toInt() ?: 0
        maxBalls = overs * 6

        // Initialize the text views
        inningsTextView = findViewById(R.id.innings)
        battingTeamTextView = findViewById(R.id.batting_team)
        oversTextView = findViewById(R.id.overs)
        scoreTextView = findViewById(R.id.score)

        inningsTextView.text = "Innings: ${intent.getIntExtra("INNINGS", 1)}"
        battingTeamTextView.text = intent.getStringExtra("BATTING_TEAM") ?: "Team_1"
        oversTextView.text = overs.toString()

        val homeTeamName = intent.getStringExtra("HOME_TEAM") ?: "Team_1"
        val awayTeamName = intent.getStringExtra("AWAY_TEAM") ?: "Team_2"

        val matchTeamsTextView: TextView = findViewById(R.id.tv_match_teams)
        matchTeamsTextView.text = "$homeTeamName vs $awayTeamName"

        // Initialize the Batsman RecyclerView
        batsmanRecyclerView = findViewById(R.id.batsman_recycler_view)
        batsmanRecyclerView.layoutManager = LinearLayoutManager(this)
        batsmanAdapter = BatsmanAdapter(arrayListOf())
        batsmanRecyclerView.adapter = batsmanAdapter

        // Initialize the Bowler RecyclerView
        bowlerRecyclerView = findViewById(R.id.bowler_recycler_view)
        bowlerRecyclerView.layoutManager = LinearLayoutManager(this)
        bowlerAdapter = BowlerAdapter(arrayListOf())
        bowlerRecyclerView.adapter = bowlerAdapter

        batsmen = arrayListOf(
            Batsman("Batsman 1", true, 0, 0, 0, 0, 0.0, false),
            Batsman("Batsman 2", false, 0, 0, 0, 0, 0.0, false)
        )
        bowler = arrayListOf(
            Bowler("Bowler 1", 0.0, 0, 0, 0, 0.0, true)
        )

        inningsTextView.text = "Innings: $innings"

        bindAdapters()
        swapBatsman()
        retireBatsman()

        retireButton.setOnClickListener {
            retireBatsman()
        }

        swapButton.setOnClickListener {
            swapBatsman()
        }

        undoButton.setOnClickListener {
            undoLastRun()
        }


        updateScore()
        updateOvers()
    }


    // Function to set the run button listeners
    private fun setRunButtonListeners() {
        run1.setOnClickListener { addRun("1") }
        run2.setOnClickListener { addRun("2") }
        run3.setOnClickListener { addRun("3") }
        run4.setOnClickListener { addRun("4") }
        run6.setOnClickListener { addRun("6") }
        run0.setOnClickListener { addRun("0") }
    }


    // Function to set the extra listeners
    private fun setExtraListeners() {
        retireButton.setOnClickListener { retireBatsman() }
        swapButton.setOnClickListener { swapBatsman() }
    }


    // Function to add a run to the adapter
    private fun addRun(run: String) {
        if (ballsBowled >= maxBalls) {
            endInnings()
            return
        }

        val isWide = wideCheckBox.isChecked
        val isNoBall = noBallCheckBox.isChecked
        val wasWicket = wicketCheckBox.isChecked

        if (wasWicket) {
            totalWickets += 1
            ballsBowled += 1
            validBallsCount += 1
            wicketCheckBox.isChecked = false
        } else if (run == "run_out") {
            totalWickets += 1
            ballsBowled += 1
            validBallsCount += 1
        } else {
            if (isWide || isNoBall) {
                totalRuns += 1
                if (isWide) wideCheckBox.isChecked = false
                if (isNoBall) noBallCheckBox.isChecked = false
            } else {
                ballsBowled += 1
                validBallsCount += 1
            }

            val isByeOrLegBye = byesCheckBox.isChecked || legByesCheckBox.isChecked

            if (isByeOrLegBye) {
                totalRuns += run.toInt()
                byesCheckBox.isChecked = false
                legByesCheckBox.isChecked = false
                updateBatsmanScores(run.toInt(), isWide, isNoBall, isByeOrLegBye = true)
                updateBowlerScores(run.toInt(), isWide, isNoBall, isByeOrLegBye = true)
            } else {
                totalRuns += run.toInt()
                updateBatsmanScores(run.toInt(), isWide, isNoBall, isByeOrLegBye = false)
                updateBowlerScores(run.toInt(), isWide, isNoBall, isByeOrLegBye = false)
            }

            history.add(Balls(run.toInt(), isWide, isNoBall, isByeOrLegBye, wasWicket))

            if (run.toInt() == 1 || run.toInt() == 3) {
                changeStrike()
            }

            adapter.addRun(run, isWide, isNoBall)
        }

        if (validBallsCount % 6 == 0 && !isWide && !isNoBall) {
            updateOvers()
        }

        updateScore()
        updateOvers()

        if (ballsBowled >= maxBalls) {
            endInnings()
        }
    }


    // Function to update the score
    private fun updateScore() {
        scoreTextView.text = getString(R.string.total_score, totalRuns, totalWickets)
    }

    // Function to update the overs
    private fun updateOvers() {
        val completedOvers = validBallsCount / 6
        val remainingBalls = validBallsCount % 6
        oversTextView.text = getString(R.string.overs_format, completedOvers, remainingBalls)
    }


    // Function to update the scores of batsmen
    private fun updateBatsmanScores(run: Int, isWide: Boolean, isNoBall: Boolean, isByeOrLegBye: Boolean) {
        if (isWide || isNoBall) return

        for (batsman in batsmen) {
            if (batsman.isOnStrike) {
                if (isByeOrLegBye) {
                    batsman.balls += 1
                } else {
                    batsman.runs += run
                    batsman.balls += 1
                    if (run == 4) {
                        batsman.fours += 1
                    } else if (run == 6) {
                        batsman.sixes += 1
                    }
                    calculateStrikeRate(batsman)

                }
                break
            }
        }
        batsmanAdapter.notifyDataSetChanged()
    }


    private fun changeStrike() {
        for (i in batsmen.indices) {
            if (batsmen[i].isOnStrike) {
                batsmen[i].isOnStrike = false
                val nextIndex = (i + 1) % batsmen.size
                batsmen[nextIndex].isOnStrike = true
                break
            }
        }
        batsmanAdapter.notifyDataSetChanged()
    }

    //Function to update the bowler performance
    private fun updateBowlerScores(run: Int, isWide: Boolean, isNoBall: Boolean, isByeOrLegBye: Boolean) {
        for (bowler in bowler) {
            if (bowler.isBowling) {
                if (!isWide && !isNoBall) {
                    val currentBalls = ((bowler.overs.toInt() * 6) + ((bowler.overs - bowler.overs.toInt()) * 10).toInt())
                    val newBalls = currentBalls + 1
                    val newOvers = newBalls / 6
                    val newBallPart = newBalls % 6
                    bowler.overs = newOvers + newBallPart / 10.0
                    bowler.runsGiven += run
                    bowler.economyRate = if (bowler.overs > 0) bowler.runsGiven / bowler.overs else 0.0
                } else if (isWide || isNoBall) {
                    bowler.runsGiven += 1
                }
                if (wicketCheckBox.isChecked) {
                    bowler.wickets += 1
                    wicketCheckBox.isChecked = false
                }
                break
            }
        }
        bowlerAdapter.notifyDataSetChanged()
    }



    // Function to calculate the strike rate
    private fun calculateStrikeRate(batsman: Batsman) {
        val totalBalls = batsman.balls
        val totalRuns = batsman.runs

        if (totalBalls > 0) {
            batsman.strikeRate = String.format("%.2f", (totalRuns.toFloat() / totalBalls.toDouble()) * 100).toDouble()
        }
    }


    // Function to retire a batsman
    private fun retireBatsman() {
        val currentBatsman = batsmen.find { it.isOnStrike }
        currentBatsman?.retired = true
        swapBatsman()
    }


    // Function to swap the batsman
    private fun swapBatsman() {
        val currentOnStrike = batsmen.find { it.isOnStrike }
        val nextBatsman = batsmen.find { !it.isOnStrike }

        if (currentOnStrike != null && nextBatsman != null) {
            currentOnStrike.isOnStrike = false
            nextBatsman.isOnStrike = true

            batsmanAdapter.notifyDataSetChanged()
        }
    }


    //Function to undo the last run and ball
    private fun undoLastRun() {
        if (history.isEmpty()) return

        val lastAction = history.removeAt(history.size - 1)

        if (lastAction.wasWicket) {
            totalWickets -= 1
            ballsBowled -= 1
            validBallsCount -= 1
        } else {
            if (lastAction.isWide || lastAction.isNoBall) {
                totalRuns -= 1
            } else {
                ballsBowled -= 1
                validBallsCount -= 1
            }

            if (lastAction.isByeOrLegBye) {
                totalRuns -= lastAction.run
            } else {
                totalRuns -= lastAction.run
                for (batsman in batsmen) {
                    if (batsman.isOnStrike) {
                        batsman.runs -= lastAction.run
                        batsman.balls -= 1
                        if (lastAction.run == 4) {
                            batsman.fours -= 1
                        } else if (lastAction.run == 6) {
                            batsman.sixes -= 1
                        }
                        calculateStrikeRate(batsman)
                        break
                    }
                }
            }
        }

        if (validBallsCount % 6 == 5) {
            updateOvers()
        }

        adapter.removeLastRun()

        updateScore()
        updateOvers()
        batsmanAdapter.notifyDataSetChanged()
        adapter.notifyDataSetChanged()
    }


    // Function to end the innings
    private fun endInnings() {
        Toast.makeText(this, "Innings Ended", Toast.LENGTH_SHORT).show()
        resetInnings()
        startNewInnings()
    }


    // Function to start a new innings
    private fun startNewInnings() {
        if (innings == 1) {
            innings = 2
            resetInnings()
            inningsTextView.text = "Innings: $innings"
        } else {
            Toast.makeText(this, "Match Ended", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


    // Function to reset the innings
    private fun resetInnings() {
        Handler(Looper.getMainLooper()).postDelayed({
            totalRuns = 0
            totalWickets = 0
            ballsBowled = 0
            validBallsCount = 0
            adapter.clearRuns()
            updateScore()
            updateOvers()
            batsmen.forEach {
                it.runs = 0
                it.balls = 0
                it.fours = 0
                it.sixes = 0
                it.strikeRate = 0.0
                it.isOnStrike = false
                it.retired = false
            }
            bowler.forEach {
                it.overs = 0.0
                it.runsGiven = 0
                it.wickets = 0
                it.economyRate = 0.0
            }
            if (batsmen.isNotEmpty()) {
                batsmen[0].isOnStrike = true
                if (batsmen.size > 1) {
                    batsmen[1].isOnStrike = false
                }
            }
            bindAdapters()
        }, 5000)
    }


    // Function to bind the adapter to the RecyclerView
    private fun bindAdapters() {
        batsmanAdapter.updateBatsmen(batsmen)
        bowlerAdapter.updateBowlers(bowler)
    }

    fun onBackButtonClick(view: View) {
        finish()
    }
}
