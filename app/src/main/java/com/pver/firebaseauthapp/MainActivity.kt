package com.pver.firebaseauthapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        window.statusBarColor = android.graphics.Color.BLACK


        auth = FirebaseAuth.getInstance()

        // Welcome message and user email
        val welcomeTextView = findViewById<TextView>(R.id.welcomeTextView)
        val userEmailTextView = findViewById<TextView>(R.id.userEmailTextView)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Set Welcome message
        welcomeTextView.text = "Welcome to the App!"

        // Display logged-in user's email
        val user = auth.currentUser
        userEmailTextView.text = "Logged in as: ${user?.email}"

        // Logout button action
        logoutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            finish()  // Close this activity and go back to login screen
        }
    }
}
