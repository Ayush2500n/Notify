package com.example.notify.notifyNotes.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notify.R
import com.example.notify.databinding.MainActivityBinding
import com.example.notify.notifyNotes.repository.NoteRepo
import com.example.notify.notifyNotes.ui.adapter.noteAdapter
import com.example.notify.notifyNotes.ui.notes.AllNotesFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllNotesFragment: Fragment(R.layout.main_activity) {
    private var _binding: MainActivityBinding? = null
    val binding: MainActivityBinding?
        get() = _binding
    private lateinit var noteAdapter: noteAdapter
    private val noteViewModel: NoteViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MainActivityBinding.bind(view)
        (activity as AppCompatActivity).setSupportActionBar(binding!!.toolbar)
        binding!!.newNoteFab.setOnClickListener {
            findNavController().navigate(R.id.action_allNotes_to_newNoteFragment2)
        }
        setupRecyclerView()
        subscribeToNotes()
    }
    private fun setupRecyclerView(){
        noteAdapter = noteAdapter()
        noteAdapter.setOnItemClickListener {
            val action = AllNotesFragmentDirections.actionAllNotesToNewNoteFragment2()
            findNavController().navigate(action)
        }
        binding?.notesRv?.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }
    private fun subscribeToNotes() = lifecycleScope.launch{
        noteViewModel.notes.collect {
            noteAdapter.notes = it
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.drop_down,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.account-> {
                findNavController().navigate(R.id.action_allNotes_to_userinfoFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}