package com.example.imagesearchpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearchpage.databinding.ItemImgListBinding

class Adapter(var mItem: MutableList<Search>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.ViewHolder {
        val binding = ItemImgListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = mItem.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mItem[position])
    }

    inner class ViewHolder(val binding: ItemImgListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Search) {
            binding.tvType.text = item.type
            binding.tvName.text = item.title
        }
    }
}