package com.example.sponsorhub.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sponsorhub.data.model.User
import com.example.sponsorhub.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private val _loginSuccess =
        MutableStateFlow(false)

    val loginSuccess =
        _loginSuccess.asStateFlow()

    private val _registerSuccess =
        MutableStateFlow(false)

    val registerSuccess =
        _registerSuccess.asStateFlow()

    private val _currentUser =
        MutableStateFlow<User?>(null)

    val currentUser =
        _currentUser.asStateFlow()

    fun login(
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true

            val result =
                repository.login(
                    email,
                    password
                )

            if (result.isSuccess) {

                val user =
                    repository.getCurrentUser()

                _currentUser.value = user

                _loginSuccess.value = true

                _message.value =
                    "Login berhasil"

            } else {

                _loginSuccess.value = false

                _message.value =
                    result.exceptionOrNull()?.message
                        ?: "Login gagal"
            }

            _isLoading.value = false
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        role: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true

            val result =
                repository.register(
                    name,
                    email,
                    password,
                    role
                )

            if (result.isSuccess) {

                _registerSuccess.value =
                    true

                _message.value =
                    "Register berhasil"

            } else {

                _registerSuccess.value =
                    false

                _message.value =
                    result.exceptionOrNull()?.message
                        ?: "Register gagal"
            }

            _isLoading.value = false
        }
    }

    fun resetState() {

        _loginSuccess.value = false

        _registerSuccess.value = false

        _message.value = ""
    }
}