package com.pver.firebaseauthapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

// asdf

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginTextView = findViewById<TextView>(R.id.loginTextView)
        val resendEmailButton = findViewById<Button>(R.id.resendEmailButton)
        val verificationInfoTextView = findViewById<TextView>(R.id.verificationInfoTextView)


        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            else if (!email.contains("@vitstudent.ac.in")) {
                Toast.makeText(this, "Invalid email address. Must use VIT mail.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser

                        user?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                            if (verifyTask.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Verification email sent to ${user.email}",
                                    Toast.LENGTH_LONG
                                ).show()

                                // Show resend button and message
                                val verificationInfoTextView = findViewById<TextView>(R.id.verificationInfoTextView)
                                val resendEmailButton = findViewById<Button>(R.id.resendEmailButton)

                                verificationInfoTextView.text = "Verification email sent to ${user.email}. Didn't get it?"
                                resendEmailButton.visibility = View.VISIBLE

                                resendEmailButton.setOnClickListener {
                                    user.sendEmailVerification().addOnCompleteListener { resendTask ->
                                        if (resendTask.isSuccessful) {
                                            Toast.makeText(this, "Verification email resent.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this, "Resend failed: ${resendTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }

                                auth.signOut()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }


        }

        loginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
