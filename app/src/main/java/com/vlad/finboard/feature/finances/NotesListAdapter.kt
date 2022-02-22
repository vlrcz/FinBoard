package com.vlad.finboard.feature.finances

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vlad.finboard.databinding.ItemDateBinding
import com.vlad.finboard.databinding.ItemNoteBinding

class NotesListAdapter(
    private val onItemClicked: (note: NoteModel) -> Unit
) :
    ListAdapter<NoteModel, ViewHolder>(NotesDiffUtilCallback()) {

    companion object {
        const val VIEW_TYPE_DATE = 1
        const val VIEW_TYPE_NOTE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isDate) {
            VIEW_TYPE_DATE
        } else {
            VIEW_TYPE_NOTE
        }
    }

    class NotesListViewHolder(
        private val binding: ItemNoteBinding,
        private val onItemClicked: (note: NoteModel) -> Unit
    ) : ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val item =
                    (bindingAdapter as? NotesListAdapter)?.getItem(bindingAdapterPosition)
                        ?: return@setOnClickListener

                onItemClicked.invoke(item)
            }
        }

        fun bind(note: NoteModel) {
            binding.noteSumTxt.text = note.sum
            binding.noteNameTxt.text = note.categoryName
            binding.noteImg.setBackgroundColor(note.categoryColor)
            Glide.with(itemView.context)
                .load(note.categoryDrawable)
                .into(binding.noteImg)
        }
    }

    class DateViewHolder(
        private val binding: ItemDateBinding
    ) : ViewHolder(binding.root) {

        fun bind(note: NoteModel) {
            binding.dateNoteTxt.text = note.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_NOTE -> {
                val binding =
                    ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                NotesListViewHolder(binding, onItemClicked)
            }
            else -> {
                val binding =
                    ItemDateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DateViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is NotesListViewHolder -> {
                holder.bind(getItem(position))
            }
            is DateViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    class NotesDiffUtilCallback : DiffUtil.ItemCallback<NoteModel>() {
        override fun areItemsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteModel, newItem: NoteModel): Boolean {
            return oldItem == newItem
        }
    }
}