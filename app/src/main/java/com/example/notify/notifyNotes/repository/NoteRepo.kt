package com.example.notify.notifyNotes.repository

import com.example.notify.notifyNotes.data.local.models.localNotes
import com.example.notify.notifyNotes.data.remote.models.User
import com.example.notify.notifyNotes.utils.Result
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton


interface NoteRepo {
    suspend fun createUser(user: User): Result<String>
    suspend fun login(user: User): Result<String>
    suspend fun getUser(): Result<User>
    suspend fun logout(): Result<String>
    suspend fun createNote(note: localNotes): Result<String>
    suspend fun updateNote(note: localNotes): Result<String>
    fun getAllNotes() : Flow<List<localNotes>>
    suspend fun getAllNotesServer()


}