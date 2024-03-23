package com.example.notify.notifyNotes.ui.notes

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.notify.R
import com.example.notify.databinding.NewNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class newNoteFragment: Fragment(R.layout.new_note){
    private var _binding: NewNoteBinding? = null
    val binding: NewNoteBinding?
        get() = _binding
    val noteViewModel: NoteViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = NewNoteBinding.bind(view)
        noteViewModel.oldNote?.noteTitle.let {
            binding?.newNoteTitle?.setText(it)
        }
        noteViewModel.oldNote?.noteDes.let {
            binding?.newNoteDes?.setText(it)
        }
        binding?.timestamp?.isVisible = noteViewModel.oldNote != null
        noteViewModel.oldNote?.date?.let {
            binding?.timestamp?.text = noteViewModel.time_change(it)
        }
        if (noteViewModel.oldNote == null){
            binding?.saveFab?.setOnClickListener {
                createNote()
            }
        }else{
            binding?.saveFab?.setOnClickListener {
                updateNote()
            }
        }
    }

    /*override fun onPause() {
        super.onPause()
        if (noteViewModel.oldNote == null){
            binding?.saveFab?.setOnClickListener {
                createNote()
            }
        }else{
            binding?.saveFab?.setOnClickListener {
                updateNote()
            }
        }
    }*/
    private fun createNote(){
        val noteTitle = binding?.newNoteTitle?.text.toString()
        val description = binding?.newNoteDes?.text.toString()
        if (noteTitle.isEmpty() && description.isEmpty()) {
            Toast.makeText(requireContext(), "Empty Note", Toast.LENGTH_SHORT).show()
            return
        }
        noteViewModel.createNote(noteTitle,description)
        }
    private fun updateNote(){
        val noteTitle = binding?.newNoteTitle?.text.toString()
        val description = binding?.newNoteDes?.text.toString()
        if (noteTitle.isEmpty() && description.isEmpty()) {
            //todo
            return
        }
        noteViewModel.updateNote(noteTitle,description)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}