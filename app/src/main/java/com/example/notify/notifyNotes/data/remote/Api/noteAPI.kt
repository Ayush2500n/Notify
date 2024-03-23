package com.example.notify.notifyNotes.data.remote.Api

import com.example.notify.notifyNotes.data.remote.models.User
import com.example.notify.notifyNotes.data.remote.models.remoteNotes
import com.example.notify.notifyNotes.data.remote.models.simpleResponse
import com.example.notify.notifyNotes.utils.Constants.API_VERSION
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface noteAPI {
    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/users/register")
    suspend fun createAccount(@Body user: User): simpleResponse

    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/users/login")
    suspend fun login(@Body user: User): simpleResponse

//////////////////////////////////////////NOTES/////////////////////////////////////////////////////

    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/notes/create")
    suspend fun createNote(@Header("Authorization") token: String,
        @Body notes: remoteNotes
    ): simpleResponse

    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/notes")
    suspend fun getNote(@Header("Authorization") token: String
    ): List<remoteNotes>

    @Headers("Content-Type: application/json")
    @POST("$API_VERSION/notes/update")
    suspend fun updateNote(@Header("Authorization") token: String,
                           @Body notes: remoteNotes
    ): simpleResponse

    @Headers("Content-Type: application/json")
    @DELETE("$API_VERSION/notes/delete")
    suspend fun deleteNote(@Header("Authorization") token: String,
                           @Query("id")noteId: String): simpleResponse
}