package com.example.notify.notifyNotes.di

import android.content.Context
import androidx.room.Room
import com.example.notify.notifyNotes.data.local.dao.noteDao
import com.example.notify.notifyNotes.data.remote.Api.noteAPI
import com.example.notify.notifyNotes.repository.NoteRepo
import com.example.notify.notifyNotes.repository.noteRepoImpl
import com.example.notify.notifyNotes.utils.Constants.BASE_URL
import com.example.notify.notifyNotes.utils.sessionManager
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.notify.notifyNotes.data.local.dao.noteDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object appModule {

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideSessionManager(
        @ApplicationContext
        context: Context
    ) = sessionManager(context)

    @Singleton
    @Provides
    fun provideNoteDatabase(@ApplicationContext context: Context): noteDatabase =
        Room.databaseBuilder(
            context,
            noteDatabase::class.java, "note_db"
        ).fallbackToDestructiveMigration().build()


    @Singleton
    @Provides
    fun provideNoteDao(noteDatabase: noteDatabase) = noteDatabase.getNoteDao()

    @Singleton
    @Provides
    fun provideNoteApi(): noteAPI {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
        return Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build().create(
            noteAPI::class.java
        )
    }

    @Singleton
    @Provides
    fun provideNoteRepo(
        noteApi: noteAPI,
        noteDao: noteDao,
        sessionManager: sessionManager
    ): NoteRepo {
        return noteRepoImpl(
            noteApi,
            noteDao,
            sessionManager
        )
    }
}