package com.example.notify.notifyNotes.ui.account

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.notify.R
import com.example.notify.databinding.LoginBinding
import com.example.notify.notifyNotes.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.login) {
    private var _binding: LoginBinding? = null
    val binding: LoginBinding?
        get() = _binding
    private val userViewModel: userViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = LoginBinding.bind(view)
        subscribetoLoginEvents()
        binding?.Loginbutton?.setOnClickListener {
            val name = binding!!.username.text.toString()
            val password = binding!!.passwordLogin.text.toString()
            userViewModel.loginUser(
                name.trim(),
                password.trim()
            )
        }
    }
    private fun subscribetoLoginEvents() = lifecycleScope.launch {
        userViewModel.loginState.collect{
            when(it){
                is Result.Error -> {
                    hideProgress()
                    Toast.makeText(requireContext(),it.errorMessage?: "Some error occurred while logging in!", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showProgress()
                }
                is Result.Success -> {
                    hideProgress()
                    Toast.makeText(requireContext(),"Logged in successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        }
    }
    private fun showProgress(){
        binding?.loginLoad?.isVisible = true
    }
    private fun hideProgress(){
        binding?.loginLoad?.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}