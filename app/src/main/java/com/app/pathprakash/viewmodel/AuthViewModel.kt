package com.app.pathprakash.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.pathprakash.data.auth.AuthRepository
import com.app.pathprakash.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository =
        AuthRepository(application.applicationContext)


    // -----------------------------------------------------
    // Loading - Login
    // -----------------------------------------------------

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading


    // -----------------------------------------------------
    // Session Checking
    // -----------------------------------------------------

    private val _isCheckingSession =
        MutableStateFlow(true)

    val isCheckingSession: StateFlow<Boolean> =
        _isCheckingSession


    // -----------------------------------------------------
    // Error Message
    // -----------------------------------------------------

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> =
        _errorMessage


    // -----------------------------------------------------
    // Logged-in User Profile
    // -----------------------------------------------------

    private val _userProfile =
        MutableStateFlow<UserProfile?>(null)

    val userProfile: StateFlow<UserProfile?> =
        _userProfile


    // -----------------------------------------------------
    // CHECK EXISTING LOGIN SESSION
    // -----------------------------------------------------

    init {
        restoreSession()
    }


    private fun restoreSession() {

        viewModelScope.launch {

            _isCheckingSession.value = true

            val result =
                repository.getCurrentUserProfile()

            result
                .onSuccess { profile ->

                    _userProfile.value = profile
                }
                .onFailure { error ->

                    _userProfile.value = null

                    _errorMessage.value =
                        error.message
                }

            _isCheckingSession.value = false
        }
    }


    // -----------------------------------------------------
    // Email + Password Login
    // -----------------------------------------------------

    fun loginWithEmail(
        email: String,
        password: String
    ) {

        viewModelScope.launch {

            _isLoading.value = true
            _errorMessage.value = null

            val result =
                repository.signInWithEmail(
                    email = email,
                    password = password
                )

            result
                .onSuccess { profile ->

                    _userProfile.value =
                        profile
                }
                .onFailure { error ->

                    _userProfile.value = null

                    _errorMessage.value =
                        error.message
                            ?: "Login failed."
                }

            _isLoading.value = false
        }
    }


    // -----------------------------------------------------
    // Google Login
    // -----------------------------------------------------

    fun loginWithGoogle() {

        viewModelScope.launch {

            _isLoading.value = true
            _errorMessage.value = null

            val result =
                repository.signInWithGoogle()

            result
                .onSuccess { profile ->

                    _userProfile.value =
                        profile
                }
                .onFailure { error ->

                    _userProfile.value = null

                    _errorMessage.value =
                        error.message
                            ?: "Google login failed."
                }

            _isLoading.value = false
        }
    }


    // -----------------------------------------------------
    // Clear Error
    // -----------------------------------------------------

    fun clearError() {

        _errorMessage.value = null
    }


    // -----------------------------------------------------
    // Logout
    // -----------------------------------------------------

    fun logout() {

        repository.signOut()

        _userProfile.value = null
        _errorMessage.value = null
    }
}