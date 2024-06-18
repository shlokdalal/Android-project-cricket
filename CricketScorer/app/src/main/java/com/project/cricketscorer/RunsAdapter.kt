package com.project.cricketscorer

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RunsAdapter(private var runList: ArrayList<String>) : RecyclerView.Adapter<RunsAdapter.RunViewHolder>() {

    private var overComplete: Boolean = false
    private var validBallsCount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_run, parent, false)
        return RunViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = runList[position]
        holder.runTextView.text = run
    }

    override fun getItemCount(): Int {
        return runList.size
    }

    fun addRun(run: String, isWide: Boolean = false, isNoBall: Boolean = false) {
        if (overComplete) return

        // Add run for display but do not count it as a valid ball if it is wide or no-ball
        runList.add(run)
        notifyItemInserted(runList.size - 1)

        if (!isWide && !isNoBall) {
            validBallsCount++
        }

        if (validBallsCount == 6) {
            overComplete = true
            Handler(Looper.getMainLooper()).postDelayed({
                clearRuns()
            }, 3000)
        }
    }

    fun removeLastRun() {
        if (runList.isNotEmpty()) {
            runList.removeAt(runList.size - 1)
            notifyItemRemoved(runList.size)

            if (validBallsCount > 0) {
                validBallsCount--
            }

            overComplete = false
        }
    }

    fun clearRuns() {
        runList.clear()
        overComplete = false
        validBallsCount = 0
        notifyDataSetChanged()
    }

    class RunViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val runTextView: TextView = view.findViewById(R.id.run_text)
    }
}
