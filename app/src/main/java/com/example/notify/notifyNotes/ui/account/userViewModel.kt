package com.example.notify.notifyNotes.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notify.notifyNotes.data.remote.models.User
import com.example.notify.notifyNotes.repository.NoteRepo
import com.example.notify.notifyNotes.utils.Constants.MAXIMUM_PASS
import com.example.notify.notifyNotes.utils.Constants.MINIMUM_PASS
import com.example.notify.notifyNotes.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class userViewModel @Inject constructor(val noteRepo: NoteRepo): ViewModel() {
    private val _registerState = MutableSharedFlow<Result<String>>()
    val registerState: SharedFlow<Result<String>> = _registerState

    private val _loginState = MutableSharedFlow<Result<String>>()
    val loginState: SharedFlow<Result<String>> = _loginState

    private val _currentUserState = MutableSharedFlow<Result<User>>()
    val currentUserState: SharedFlow<Result<User>> = _currentUserState

    fun createUser(name: String, email: String, password: String, confirmPassword: String) = viewModelScope.launch{
        _registerState.emit(Result.Loading())
        if (name.isEmpty()||email.isEmpty()||password != confirmPassword||confirmPassword.isEmpty()){
            _registerState.emit(Result.Error("Some Fields are Empty!"))
            return@launch
        }
        if (!isEmailValid(email)){
            _registerState.emit(Result.Error("Invalid Username"))
            return@launch
        }
        if(!isPasswordValid(password)){
            _registerState.emit((Result.Error("Password should be between $MINIMUM_PASS and $MAXIMUM_PASS characters")))
            return@launch
        }
        val newUser = User(name,email, password)
        _registerState.emit(noteRepo.createUser(newUser))
        _registerState.emit(Result.Success("User added"))
    }
    fun loginUser(email: String, password: String) = viewModelScope.launch{
        _loginState.emit(Result.Loading())
        if (email.isEmpty()||password.isEmpty()){
            _loginState.emit(Result.Error("Some Fields are Empty!"))
            return@launch
        }
        if (!isEmailValid(email)){
            _loginState.emit(Result.Error("Invalid Username"))
            return@launch
        }
        if(!isPasswordValid(password)){
            _loginState.emit((Result.Error("Incorrect Password")))
            return@launch
        }
        val newUser = User(name = null,email, password)
        _registerState.emit(noteRepo.login(newUser))
        _registerState.emit(Result.Success("User added"))
    }

    fun getCurrentUser() = viewModelScope.launch {
        _currentUserState.emit(Result.Loading())
        _currentUserState.emit(noteRepo.getUser())
    }
    fun logout() = viewModelScope.launch {
        val result = noteRepo.logout()
        if(result is Result.Success){
            getCurrentUser()
        }
    }
    private fun isEmailValid(email: String): Boolean{
        var regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
        val pattern = Pattern.compile(regex)
        return (email.isNotEmpty() && pattern.matcher(email).matches())
    }
    private fun isPasswordValid(password: String): Boolean{
        return (password.length in MINIMUM_PASS..MAXIMUM_PASS)
    }

}

