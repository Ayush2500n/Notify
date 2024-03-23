package com.example.notify.notifyNotes.repository

import com.example.notify.notifyNotes.data.local.dao.noteDao
import com.example.notify.notifyNotes.data.local.models.localNotes
import com.example.notify.notifyNotes.data.remote.Api.noteAPI
import com.example.notify.notifyNotes.data.remote.models.User
import com.example.notify.notifyNotes.data.remote.models.remoteNotes
import com.example.notify.notifyNotes.utils.Result
import com.example.notify.notifyNotes.utils.isNetworkConnected
import com.example.notify.notifyNotes.utils.sessionManager
import com.example.notify.notifyNotes.di.appModule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class noteRepoImpl @Inject constructor(
    val noteAPI: noteAPI,
    val noteDao: noteDao,
    val sessionManager: sessionManager
): NoteRepo {
    override suspend fun createUser(user: User): Result<String> {
        return try {
            if (!isNetworkConnected(sessionManager.context)){
                Result.Error<String>("No Internet Connection")
            }
            val result = noteAPI.createAccount(user)
            if (result.success){
                sessionManager.updateSession(result.message,user.email,user.name?: "")
                Result.Success("User Created Successfully")
            }else{
                Result.Error<String>(result.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            Result.Error<String>(e.message ?: "Some Problem Occurred while registering new user")
        }
    }
    override suspend fun login(user: User): Result<String> {
        return try {
            if (!isNetworkConnected(sessionManager.context)){
                Result.Error<String>("No Internet Connection")
            }
            val result = noteAPI.login(user)
            if (result.success){
                sessionManager.updateSession(result.message,user.email,user.name?: "")
                Result.Success("Logged in Successfully")
            }else{
                Result.Error<String>(result.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            Result.Error<String>(e.message ?: "Some Problem Occurred while logging in")
        }
    }
    override suspend fun getUser(): Result<User> {
        return try {
            val name = sessionManager.getCurrentName()
            val email = sessionManager.getCurrentEmail()
            if (name == null || email == null){
                Result.Error<User>("User not logged in!")
            }else{
                Result.Success(User(name,email!!,""))
            }
        }catch (e:Exception){
            e.printStackTrace()
            Result.Error("Some Error Occurred while getting current user details")
        }
    }
    override suspend fun logout(): Result<String> {
        return try {
            sessionManager.logout()
            Result.Success("Logged out Successfully")
        }catch (e:Exception){
            e.printStackTrace()
            Result.Error(e.message?:"Some Problem occurred while logging out")
        }
    }
    override suspend fun createNote(note: localNotes): Result<String> {
        try {

            noteDao.insertNote(note)
            val token = sessionManager.getJWTToken() ?: return Result.Success("Note Saved in Local Database")
            if(!isNetworkConnected(sessionManager.context)){
                return Result.Error("No Internet Connection")
            }
            val result = noteAPI.createNote("Bearer $token", remoteNotes(note.noteTitle,note.noteDes,note.date,note.noteId))
            return if (result.success){
                noteDao.insertNote(note.also { it.connected = true })
                Result.Success("Note Added Successfully")
            }
            else{
                Result.Error(result.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            return Result.Error(e.message?:"Some Problem Occurred while creating Note")
        }
    }
    override suspend fun updateNote(note: localNotes): Result<String> {
        try {
            noteDao.insertNote(note)
            val token = sessionManager.getJWTToken()
                ?: return Result.Success("Note Updated in Local Database")
            if(!isNetworkConnected(sessionManager.context)){
                return Result.Error("No Internet Connection")
            }
            val result = noteAPI.updateNote("Bearer $token", remoteNotes(note.noteTitle,note.noteDes,note.date,note.noteId))
            return if (result.success){
                noteDao.insertNote(note.also { it.connected = true })
                Result.Success("Note Updated Successfully")
            }
            else{
                Result.Error(result.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
            return Result.Error(e.message?:"Some Problem Occurred")
        }
    }

    override fun getAllNotes(): Flow<List<localNotes>> = noteDao.getAllNotes()

    override suspend fun getAllNotesServer() {
        val token = sessionManager.getJWTToken() ?: return
        if(!isNetworkConnected(sessionManager.context)){
            return
        }
        val result = noteAPI.getNote("Bearer $token")
        result.forEach {
            noteDao.insertNote(localNotes(
                noteTitle = it.title,
                noteDes = it.description,
                date = it.date,
                connected = true,
                noteId = it.id
            ))
        }
    }
}