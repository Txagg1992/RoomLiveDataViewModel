package com.curiousca.roomlivedataviewmodel.DataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
class Note {
    @PrimaryKey(autoGenerate = true)
    private var id = 0
    private var title: String? = null
    private var description: String? = null
    private var priority = 0
    private var date: String? = null

    fun Note(
        title: String?,
        description: String?,
        priority: Int,
        date: String?
    ) {
        this.title = title
        this.description = description
        this.priority = priority
        this.date = date
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getId(): Int {
        return id
    }

    fun getTitle(): String? {
        return title
    }

    fun getDescription(): String? {
        return description
    }

    fun getPriority(): Int {
        return priority
    }

    fun getDate(): String? {
        return date
    }
}