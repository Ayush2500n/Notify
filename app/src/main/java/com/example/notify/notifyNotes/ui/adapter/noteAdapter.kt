package com.example.notify.notifyNotes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.notify.R
import com.example.notify.databinding.NoteBinding
import com.example.notify.notifyNotes.data.local.models.localNotes

class noteAdapter: RecyclerView.Adapter<noteAdapter.viewHolder>(){
    class viewHolder(val binding: NoteBinding): RecyclerView.ViewHolder(binding.root)

    val diffUtil = object : DiffUtil.ItemCallback<localNotes>() {
        override fun areItemsTheSame(oldItem: localNotes, newItem: localNotes): Boolean {
        return oldItem.noteId == newItem.noteId
        }

        override fun areContentsTheSame(oldItem: localNotes, newItem: localNotes): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this,diffUtil)
    var notes: List<localNotes>
        get() = differ.currentList
        set(value) = differ.submitList(value)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(NoteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return notes.size
    }
    private var onItemClickListener: ((localNotes) -> Unit)? = null
    fun setOnItemClickListener(listener: (localNotes)->Unit){
        onItemClickListener = listener
    }
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val note = notes[position]
        holder.binding.apply {
            noteTitle.isVisible = note.noteTitle != null
            noteDes.isVisible = note.noteDes != null
            note.noteTitle.let {
                noteTitle.text = it
            }
            note.noteDes.let {
                noteDes.text = it
            }
            status.setBackgroundResource(
                if (note.connected) R.drawable.online
                else R.drawable.offline
            )
            root.setOnClickListener {
                onItemClickListener?.invoke(note)
            }
        }
    }

}