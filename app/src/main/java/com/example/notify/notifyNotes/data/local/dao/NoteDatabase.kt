package com.example.notify.notifyNotes.data.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notify.notifyNotes.data.local.dao.noteDao
import com.example.notify.notifyNotes.data.local.models.localNotes

@Database(entities = [localNotes::class], version = 2, exportSchema = false)
abstract class noteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): noteDao
}
