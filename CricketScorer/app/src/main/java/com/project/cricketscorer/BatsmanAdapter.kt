package com.project.cricketscorer

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.cricketscorer.databinding.ItemBatsmanBinding

class BatsmanAdapter(private val batsmen: ArrayList<Batsman>) : RecyclerView.Adapter<BatsmanAdapter.BatsmanViewHolder>() {

    class BatsmanViewHolder(val binding: ItemBatsmanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatsmanViewHolder {
        val binding = ItemBatsmanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BatsmanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BatsmanViewHolder, position: Int) {
        val batsman = batsmen[position]
        val currentBatsman = batsmen[position]

        holder.binding.batsmanName.text = batsman.name
        holder.binding.runs.text = batsman.runs.toString()
        holder.binding.balls.text = batsman.balls.toString()
        holder.binding.fours.text = batsman.fours.toString()
        holder.binding.sixes.text = batsman.sixes.toString()
        holder.binding.strikeRate.text = batsman.strikeRate.toString()

        holder.binding.batsmanName.text = if (currentBatsman.isOnStrike) "${currentBatsman.name} *" else currentBatsman.name


        if (batsman.isOnStrike) {
            holder.binding.batsmanName.setTypeface(null, Typeface.BOLD)
        } else {
            holder.binding.batsmanName.setTypeface(null, Typeface.NORMAL)
        }
    }

    override fun getItemCount(): Int = batsmen.size

    fun updateBatsmen(newBatsmen: List<Batsman>) {
        batsmen.clear()
        batsmen.addAll(newBatsmen)
        notifyDataSetChanged()
    }
}
