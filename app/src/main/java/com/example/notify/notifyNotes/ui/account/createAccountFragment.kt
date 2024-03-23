package com.example.notify.notifyNotes.ui.account

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.notify.R
import com.example.notify.databinding.NewUserBinding
import com.example.notify.notifyNotes.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class createAccountFragment: Fragment(R.layout.new_user) {
    private var _binding: NewUserBinding? = null
    val binding: NewUserBinding?
        get() = _binding
    private val userViewModel: userViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = NewUserBinding.bind(view)
        subscribetoRegisterEvents()

        binding?.RegistrationButton?.setOnClickListener {
            val name = binding!!.name.text.toString()
            val email = binding!!.email.text.toString()
            val password = binding!!.password.text.toString()
            val confirm_pass = binding!!.confirmPass.text.toString()
            userViewModel.createUser(
                name.trim(),
                email.trim(),
                password.trim(),
                confirm_pass.trim()
            )
        }
    }
    private fun subscribetoRegisterEvents() = lifecycleScope.launch {
        userViewModel.registerState.collect{
            when(it){
                is Result.Error -> {
                    hideProgress()
                    Toast.makeText(requireContext(),it.errorMessage?:"Some error occurred while registering you",Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgress()
                }
                is Result.Success -> {
                    hideProgress()
                    Toast.makeText(requireContext(),"Account added successfully",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun showProgress(){
        binding?.newUserLoad?.isVisible = true
    }
    private fun hideProgress(){
        binding?.newUserLoad?.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}