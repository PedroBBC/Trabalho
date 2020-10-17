package com.example.healthyabj

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class ChatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chatopage)
        auth = FirebaseAuth.getInstance()






    }
}