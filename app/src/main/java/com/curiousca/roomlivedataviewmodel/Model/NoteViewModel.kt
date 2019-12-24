package com.curiousca.roomlivedataviewmodel.Model

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.curiousca.roomlivedataviewmodel.DataClasses.Note

class NoteViewModel(@NonNull application: Application) :
    AndroidViewModel(application) {
    private val repository: NoteRepository
    private val allNotes: LiveData<List<Note?>?>?
    fun insert(note: Note?) {
        repository.insert(note)
    }

    fun update(note: Note?) {
        repository.update(note)
    }

    fun delete(note: Note?) {
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun getAllNotes(): LiveData<List<Note?>?>? {
        return allNotes
    }

    init {
        repository =
            NoteRepository(
                application
            )
        allNotes = repository.getAllNotes()
    }
}
