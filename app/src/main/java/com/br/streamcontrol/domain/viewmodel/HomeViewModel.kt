package com.br.streamcontrol.domain.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.streamcontrol.domain.routes.Router
import com.br.streamcontrol.domain.routes.Screen
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {

    private val TAG = HomeViewModel::class.simpleName

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    val emailId: MutableLiveData<String> = MutableLiveData()

    val username: MutableLiveData<String> = MutableLiveData()

    val phoneNumber: MutableLiveData<String> = MutableLiveData()


    fun logout() {

        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                Log.d(TAG, "Inside sign outsuccess")
                Router.navigateTo(Screen.LoginScreen)
            } else {
                Log.d(TAG, "Inside sign out is not complete")
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)

    }

    fun checkForActiveSession() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(TAG, "Valid session")
            isUserLoggedIn.value = true
        } else {
            Log.d(TAG, "User is not logged in")
            isUserLoggedIn.value = false
        }
    }

    fun getUserData() {
        FirebaseAuth.getInstance().currentUser?.also {
            it.email?.also { email ->
                emailId.value = email
            }
            it.displayName?.also { name ->
                username.value = name
                println(username.value)
                println(name)
            }
            it.phoneNumber?.also { phone ->
                phoneNumber.value = phone
            }
        }
    }
}