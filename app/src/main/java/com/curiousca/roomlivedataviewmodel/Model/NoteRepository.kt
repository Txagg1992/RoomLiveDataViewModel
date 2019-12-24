package com.curiousca.roomlivedataviewmodel.Model

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.curiousca.roomlivedataviewmodel.DataClasses.Note
import com.curiousca.roomlivedataviewmodel.DataClasses.NoteDatabase

class NoteRepository(application: Application) {

    private var noteDao: NoteDao? = null
    private var allNotes: LiveData<List<Note?>?>? = null

    fun NoteRepository(application: Application?) {
        val database: NoteDatabase = NoteDatabase.getInstance(application)
        noteDao = database.noteDao()
        allNotes = noteDao!!.getAllNotes()
    }

    fun insert(note: Note?) {
        InsertNoteAsyncTask(
            noteDao
        ).execute(note)
    }

    fun update(note: Note?) {
        UpdateNoteAsyncTask(
            noteDao
        ).execute(note)
    }

    fun delete(note: Note?) {
        DeleteNoteAsyncTask(
            noteDao
        ).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNoteAsyncTask(
            noteDao
        ).execute()
    }

    fun getAllNotes(): LiveData<List<Note?>?>? {
        return allNotes
    }

    private class InsertNoteAsyncTask private constructor(private val noteDao: NoteDao?) :
        AsyncTask<Note?, Void?, Void?>() {
        protected override fun doInBackground(vararg notes: Note): Void? {
            noteDao!!.insert(notes[0])
            return null
        }

    }

    private class UpdateNoteAsyncTask private constructor(private val noteDao: NoteDao?) :
        AsyncTask<Note?, Void?, Void?>() {
        protected override fun doInBackground(vararg notes: Note): Void? {
            noteDao!!.update(notes[0])
            return null
        }

    }

    open class DeleteNoteAsyncTask private constructor(private val noteDao: NoteDao?) :
        AsyncTask<Note?, Void?, Void?>() {
        override fun doInBackground(vararg p0: Note?): Void? {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        } doInBackground(vararg notes: Note): Void? {
            noteDao!!.delete(notes[0])
            return null
        }

    }

    private class DeleteAllNoteAsyncTask private constructor(private val noteDao: NoteDao?) :
        AsyncTask<Void?, Void?, Void?>() {
        protected override fun doInBackground(vararg voids: Void): Void? {
            noteDao!!.deleteAllNotes()
            return null
        }

    }

}