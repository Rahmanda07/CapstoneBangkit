package com.capstone.aquamate.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.aquamate.DetailFishDictionary
import com.capstone.aquamate.api.DataItemFishDictionary
import com.capstone.aquamate.R
import com.capstone.aquamate.databinding.ActivityItemFishDictionaryBinding
import com.squareup.picasso.Picasso

class FishDictionaryAdapter : ListAdapter<DataItemFishDictionary, FishDictionaryAdapter.FishDictionaryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FishDictionaryViewHolder {
        val binding = ActivityItemFishDictionaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FishDictionaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FishDictionaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FishDictionaryViewHolder(private val binding: ActivityItemFishDictionaryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(fishItem: DataItemFishDictionary) {
            binding.apply {
                tvFishName.text = fishItem.fishName
                tvFishLatinName.text = fishItem.fishLatinName
                tvFishDescription.text = fishItem.fishDesc

                if (fishItem.fishImage.isNotBlank()) {
                    Picasso.get().load(fishItem.fishImage).into(imageFish)
                } else {
                    Picasso.get().load(R.drawable.fish_dictionary).into(imageFish)
                }

                root.setOnClickListener {
                    val context = root.context
                    val intent = Intent(context, DetailFishDictionary::class.java).apply {
                        putExtra("dictionaryId", fishItem.id)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DataItemFishDictionary>() {
        override fun areItemsTheSame(oldItem: DataItemFishDictionary, newItem: DataItemFishDictionary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItemFishDictionary, newItem: DataItemFishDictionary): Boolean {
            return oldItem == newItem
        }
    }
}
