package com.curiousca.roomlivedataviewmodel.DataClasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.curiousca.roomlivedataviewmodel.R
import java.lang.String

class NoteAdapter :
    ListAdapter<Note?, NoteAdapter.NoteHolder?>(DIFF_CALLBACK) {
    //private List<Note> notes = new ArrayList<>();
    private var listener: OnItemClickListener? = null

    @NonNull
    fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): NoteHolder {
        val itemView: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.note_item, viewGroup, false)
        return NoteHolder(itemView)
    }

    fun onBindViewHolder(@NonNull noteHolder: NoteHolder, position: Int) {
        val currentNote: Note = getItem(position)
        noteHolder.textViewTitle.text = currentNote.getTitle()
        noteHolder.textViewDescription.text = currentNote.getDescription()
        noteHolder.textViewPriority.text = String.valueOf(currentNote.getPriority())
        noteHolder.textViewDate.text = currentNote.getDate()
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    inner class NoteHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView
        val textViewDescription: TextView
        val textViewPriority: TextView
        val textViewDate: TextView

        init {
            textViewTitle = itemView.findViewById(R.id.text_view_title)
            textViewDescription = itemView.findViewById(R.id.text_view_description)
            textViewPriority = itemView.findViewById(R.id.text_view_priority)
            textViewDate = itemView.findViewById(R.id.text_view_date)
            itemView.setOnClickListener {
                val position: Int = getAdapterPosition()
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> = object : DiffUtil.ItemCallback<Note?>() {
            override fun areItemsTheSame(@NonNull oldItem: Note, @NonNull newItem: Note): Boolean {
                return oldItem.getId() === newItem.getId()
            }

            override fun areContentsTheSame(@NonNull oldItem: Note, @NonNull newItem: Note): Boolean {
                return oldItem.getTitle().equals(newItem.getTitle()) &&
                        oldItem.getDescription().equals(newItem.getDescription()) &&
                        oldItem.getPriority() == newItem.getPriority()
                        && oldItem.getDate() == newItem.getDate()
            }
        }
    }
}
