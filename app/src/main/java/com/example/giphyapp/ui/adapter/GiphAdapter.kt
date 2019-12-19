package com.example.giphyapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giphyapp.R
import com.example.giphyapp.data.api.model.Giph
import com.example.giphyapp.databinding.ItemListBinding

class GiphAdapter(private val items: List<Giph>) :
    RecyclerView.Adapter<GiphAdapter.GiphViewHolder>() {

    companion object {

        @JvmStatic
        @BindingAdapter("bind:imageUrl")
        fun loadImage(imageView: ImageView, imageUrl: String?) {
            Glide.with(imageView)
                .asGif()
                .placeholder(R.drawable.placeholder)
                .load(imageUrl)
                .centerCrop()
                .into(imageView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiphViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GiphViewHolder(binding.root)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: GiphViewHolder, position: Int) {
        if (position in items.indices) {
            holder.bind(items[position])
        }
    }

    fun notifyAdapter(event: NotifyEvent) {
        when (event) {
            is NotifyEvent.RangeInserted -> {
                notifyItemRangeInserted(itemCount - event.itemCount, itemCount)
            }
            is NotifyEvent.DatasetChanged -> notifyDataSetChanged()
        }
    }

    inner class GiphViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = DataBindingUtil.bind<ItemListBinding>(view)

        fun bind(item: Giph) {
            binding?.giph = item
        }

    }

    sealed class NotifyEvent {
        data class RangeInserted(val itemCount: Int) : NotifyEvent()
        object DatasetChanged : NotifyEvent()
    }

}