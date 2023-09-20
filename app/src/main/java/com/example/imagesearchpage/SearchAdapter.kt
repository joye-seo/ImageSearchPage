package com.example.imagesearchpage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearchpage.databinding.ItemImgListBinding

class SearchAdapter(val context : Context, var mItem: MutableList<Search>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val binding = ItemImgListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = mItem.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mItem[position])
    }

    inner class ViewHolder(val binding: ItemImgListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Search) {
            Glide.with(context)
                .load(item.image)
                .into(binding.ivProfile)
            binding.tvType.text = item.type
            binding.tvName.text = item.title

            itemView.setOnClickListener {
                Data.favoriteList.add(item)
            }
        }
    }
}