package com.sahilpc.bookie.presentation.home_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sahilpc.bookie.R
import com.sahilpc.bookie.databinding.HomeRvItemBinding
import com.sahilpc.bookie.domain.model.Note
import javax.inject.Inject

class HomeAdapter @Inject constructor() : RecyclerView.Adapter<HomeAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(val binding: HomeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            HomeRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = differ.currentList[position]
        holder.binding.note = note
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(note) }
            }
        }
    }

    private var onItemClickListener: ((Note) -> Unit)? = null

    fun setOnItemClickListener(listener: (Note) -> Unit) {
        onItemClickListener = listener
    }
}