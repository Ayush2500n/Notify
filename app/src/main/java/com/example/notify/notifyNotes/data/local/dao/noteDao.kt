package com.example.notify.notifyNotes.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notify.notifyNotes.data.local.models.localNotes
import kotlinx.coroutines.flow.Flow


@Dao
interface noteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: localNotes)

    @Query("SELECT * FROM localNotes where locallyDeleted = 0 order by date DESC ")
    fun getAllNotes(): Flow<List<localNotes>>

    @Query("SELECT * FROM localNotes where connected = 0")
    fun getLocalNotes(): Flow<List<localNotes>>

    @Query("DELETE FROM localNotes where noteId=:noteId")
    suspend fun deleteNote(noteId: String)

    @Query("UPDATE localNotes set locallyDeleted = 1 where noteId=:noteId ")
    suspend fun deleteNoteLocally(noteId: String)

}