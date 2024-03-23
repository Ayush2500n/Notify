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
import com.example.notify.databinding.UserInfoBinding
import com.example.notify.notifyNotes.utils.Result
import kotlinx.coroutines.launch

class UserinfoFragment: Fragment(R.layout.user_info) {
    private var _binding: UserInfoBinding? = null
    val binding: UserInfoBinding?
        get() = _binding
    private val userViewModel: userViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = UserInfoBinding.bind(view)
        binding?.buttonLogin?.setOnClickListener {
            findNavController().navigate(R.id.action_userinfoFragment_to_loginFragment)
        }
        binding?.submitRegister?.setOnClickListener {
            findNavController().navigate(R.id.action_userinfoFragment_to_createAccountFragment)
        }
        subscribetoUserInfoEvents()
        binding?.buttonLogout?.setOnClickListener {
            userViewModel.logout()
        }
    }

    private fun subscribetoUserInfoEvents() = lifecycleScope.launch {
        userViewModel.currentUserState.collect{
            when(it){
                is Result.Error -> {
                    userLoggedout()
                    Toast.makeText(requireContext(),it.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                }
                is Result.Success -> {
                    userLoggedin()
                    Toast.makeText(requireContext(),"Logged in successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        userViewModel.getCurrentUser()
    }
    private fun userLoggedin(){
        binding?.buttonLogin?.isVisible = false
        binding?.submitRegister?.isVisible = false
        binding?.buttonLogout?.isVisible = true
    }
    private fun userLoggedout(){
        binding?.buttonLogin?.isVisible = true
        binding?.submitRegister?.isVisible = true
        binding?.buttonLogout?.isVisible = false
        binding?.userName?.isVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}