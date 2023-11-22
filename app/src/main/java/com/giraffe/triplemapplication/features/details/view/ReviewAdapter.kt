package com.giraffe.triplemapplication.features.details.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giraffe.triplemapplication.databinding.ChipsItemBinding
import com.giraffe.triplemapplication.databinding.ReviewItemLayoutBinding
import com.giraffe.triplemapplication.model.products.Review

class ReviewAdapter :
    ListAdapter<Review, ReviewAdapter.ViewHolder>(ReviewDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ReviewItemLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentReview = getItem(position)
        holder.bind(currentReview)
    }

    class ViewHolder(private val binding: ReviewItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentReview: Review) {
            binding.date.text =currentReview.date
            binding.userName.text =currentReview.userName
            binding.message.text =currentReview.reviewContent
        }
    }

    class ReviewDiffUtil : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}
