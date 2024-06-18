package com.project.cricketscorer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.cricketscorer.databinding.ItemBowlerBinding

class BowlerAdapter(private val bowlers: ArrayList<Bowler>) : RecyclerView.Adapter<BowlerAdapter.BowlerViewHolder>() {

    class BowlerViewHolder(val binding: ItemBowlerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BowlerViewHolder {
        val binding = ItemBowlerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BowlerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BowlerViewHolder, position: Int) {
        val bowler = bowlers[position]
        holder.binding.bowlerName.text = bowler.name
        holder.binding.overs.text = bowler.overs.toString()
        holder.binding.maidens.text = bowler.maidens.toString()
        holder.binding.runs.text = bowler.runsGiven.toString()
        holder.binding.wickets.text = bowler.wickets.toString()
        holder.binding.economyRate.text = bowler.economyRate.toString()
    }

    override fun getItemCount(): Int = bowlers.size

    fun updateBowlers(newBowlers: List<Bowler>) {
        bowlers.clear()
        bowlers.addAll(newBowlers)
        notifyDataSetChanged()
    }
}

