package com.capstone.aquamate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.aquamate.R
import com.capstone.aquamate.api.recomendation

class RecomendationAdapter(private val recomendation: ArrayList<recomendation>):  RecyclerView.Adapter<RecomendationAdapter.ListViewHoder>() {
private lateinit var onItemClickCallback: OnItemClickCallback
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHoder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_rekomendasi,parent,false)
        return ListViewHoder(view)
    }

    override fun getItemCount(): Int =recomendation.size



    override fun onBindViewHolder(holder: ListViewHoder, position: Int) {
        val (name, description, photo) = recomendation[position]
        holder.imgPhoto.setImageResource(photo)
        holder.tvName.text = name
        holder.tvDescription.text = description

        holder.itemView.setOnClickListener {

            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(recomendation[position])
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: recomendation)
    }

    class ListViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView =itemView.findViewById(R.id.img_item_photo)
        val tvName : TextView = itemView.findViewById(R.id.tv_item_name)
        val tvDescription : TextView = itemView.findViewById(R.id.tv_item_description)
    }
}
