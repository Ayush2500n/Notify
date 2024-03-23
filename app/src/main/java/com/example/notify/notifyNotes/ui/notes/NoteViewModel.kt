package com.example.notify.notifyNotes.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notify.notifyNotes.data.local.models.localNotes
import com.example.notify.notifyNotes.repository.NoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(val noteRepo: NoteRepo): ViewModel() {
    var oldNote: localNotes? = null
    val notes = noteRepo.getAllNotes()
    fun createNote(noteTitle: String?, noteDes: String?)=viewModelScope.launch(Dispatchers.IO){
        val localNote = localNotes(noteTitle!!,noteDes!!)
        noteRepo.createNote(localNote)
    }
    fun updateNote(noteTitle: String?, description: String?)=viewModelScope.launch(Dispatchers.IO) {
        if (noteTitle == oldNote?.noteId && description == oldNote?.noteDes && oldNote?.connected == true) return@launch
        val note = localNotes(noteTitle!!,description!!, noteId = oldNote?.noteId!!)
        noteRepo.updateNote(note)
    }
    fun time_change(time: Long): String{
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat("hh:mm a | MMM d, yyyy", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}