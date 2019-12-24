package com.curiousca.roomlivedataviewmodel.DataClasses

import android.content.Context
import android.os.AsyncTask
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.curiousca.roomlivedataviewmodel.Model.NoteDao


@Database(entities = {Note.class}, version = 1)
class NoteDatabase {

    private var instance: NoteDatabase? = null

    fun noteDao(): NoteDao {
        return Note
    }

    @Synchronized
    fun getInstance(context: Context): NoteDatabase? {
        if (instance == null) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java, "note_database"
            )
                .fallbackToDestructiveMigration()
                .addCallback(roomCallback)
                .build()
        }
        return instance
    }

    private val roomCallback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
        fun onCreate(@NonNull db: SupportSQLiteDatabase?) {
            super.onCreate(db)
            PopulateDbAsyncTask(instance).execute()
        }
    }

    private class PopulateDbAsyncTask(db: NoteDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val noteDao: NoteDao
        override fun doInBackground(vararg voids: Void): Void? {
            noteDao.insert(Note("Title 1", "Description 1", 4, "Date"))
            noteDao.insert(Note("Title 2", "Description 2", 2, "Date"))
            noteDao.insert(Note("Title 3", "Description 3", 3, "Date"))
            return null
        }

        init {
            noteDao = db!!.noteDao()
        }
    }

}