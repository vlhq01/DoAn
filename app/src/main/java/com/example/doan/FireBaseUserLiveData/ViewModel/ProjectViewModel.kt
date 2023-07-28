package com.example.doan.FireBaseUserLiveData.ViewModel


import androidx.lifecycle.map
import androidx.lifecycle.ViewModel
import com.example.doan.FireBaseUserLiveData.FirebaseUserLiveData

class ProjectViewModel : ViewModel() {
    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }
    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}